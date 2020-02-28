package repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import models.RedactedImageMetadata;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.amazonaws.services.dynamodbv2.model.Select.ALL_ATTRIBUTES;

public class DynamoReader {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
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
        Map.Entry<String, KeysAndAttributes> entry = Map.entry(
                tableName, new KeysAndAttributes().withAttributesToGet(ALL_ATTRIBUTES.toString())
        );
        Map<String, KeysAndAttributes> requestItems = Map.ofEntries(entry);
        BatchGetItemRequest batchGetItemRequest = new BatchGetItemRequest(requestItems);
        BatchGetItemResult batchGetItemResult = dynamoDB.batchGetItem(batchGetItemRequest);

        Set<RedactedImageMetadata> imageMetadata = new HashSet<>();
        batchGetItemResult.getResponses().values().forEach(item ->
            item.forEach(attributeValueMap -> {
                imageMetadata.add(new RedactedImageMetadata(
                        attributeValueMap.get("phone-number").getS(), // TODO look up username in cognito db?
                        attributeValueMap.get("objectKey").getS(),
                        attributeValueMap.get("text").getS(),
                        DateFormat.getDateTimeInstance()
                                .format(new Date(Long.parseLong(attributeValueMap.get("date").getN())))
                ));
            })
        );
        return imageMetadata;
    }
}
