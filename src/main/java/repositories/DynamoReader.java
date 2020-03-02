package repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import models.RedactedImageMetadata;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reads from DynamoDB.
 */
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

    /**
     * Gets several attributes from each row found in the {@link this#tableName} table and maps it to a
     * {@link RedactedImageMetadata} object.
     *
     * @return Set of all {@link RedactedImageMetadata} objects found in DynamoDB table.
     */
    public Set<RedactedImageMetadata> retrieveAllImageMetadata() {
        return dynamoDB.scan(tableName, ATTRIBUTES)
                .getItems()
                .stream()
                .map(RedactedImageMetadata::fromDbEntity)
                .collect(Collectors.toSet());
    }
}
