package repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import models.RedactedImageMetadata;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DynamoReader {

    private final AmazonDynamoDB dynamoDB;
    private final String tableName;

    public DynamoReader() {
        this(AmazonDynamoDBAsyncClientBuilder.defaultClient(), System.getenv("TABLE_NAME"));
    }

    public DynamoReader(AmazonDynamoDB dynamoDB, String tableName) {
        this.dynamoDB = dynamoDB;
        this.tableName = tableName;
    }

    public Set<RedactedImageMetadata> retrieveAllImageMetadata() {
        ScanResult scanResult = dynamoDB.scan(tableName, List.of("phone-number", "objectKey", "text", "date"));
        System.out.println("Retrieved " + scanResult.getCount() + " items from DynamoDB.");
        return scanResult.getItems().stream().map(this::createMetadataObject).collect(Collectors.toSet());
    }

    private RedactedImageMetadata createMetadataObject(Map<String, AttributeValue> attributeValueMap) {
        return new RedactedImageMetadata(
                attributeValueMap.get("phone-number").getS(), // TODO look up username in cognito db?
                attributeValueMap.get("objectKey").getS(),
                attributeValueMap.get("text").getS(),
                DateFormat.getDateTimeInstance()
                        .format(new Date(Long.parseLong(attributeValueMap.get("date").getN())))
        );
    }
}
