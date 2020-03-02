package repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import models.RedactedImageMetadata;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DynamoReader {

    private static final List<String> ATTRIBUTES = List.of("phone-number", "objectKey", "text", "date");
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
        return dynamoDB.scan(tableName, ATTRIBUTES)
                .getItems()
                .stream()
                .map(RedactedImageMetadata::fromDbEntity)
                .collect(Collectors.toSet());
    }
}
