package repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import models.RedactedImageMetadata;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DynamoReaderTest {

    @Mock
    private AmazonDynamoDB mockDynamoDb;
    private String tableName = "TABLE_NAME";

    private DynamoReader dynamoReader;


    @Before
    public void setUp() {
        dynamoReader = new DynamoReader(mockDynamoDb, tableName);
    }

    @Test
    public void retrieveAllImageMetadata_GivenDataExistsInDynamo_ReturnMetadata() {
        // given
        List<String> attributesToGet = List.of("phone-number", "objectKey", "text", "date");
        String expectedPhoneNum = "+15555555555";
        List<Map<String, AttributeValue>> attributeMap = List.of(Map.of("phone-number", new AttributeValue().withS(expectedPhoneNum),
                "objectKey",  new AttributeValue().withS("1"),
                "text",  new AttributeValue().withS("text-itself"),
                "date",  new AttributeValue().withN("1582906773023")));
        ScanResult scanResult = new ScanResult().withItems(attributeMap);

        // when
        when(mockDynamoDb.scan(tableName, attributesToGet)).thenReturn(scanResult);

        Set<RedactedImageMetadata> actual = dynamoReader.retrieveAllImageMetadata();

        // then
        verify(mockDynamoDb, times(1)).scan(tableName, attributesToGet);
        Assert.assertEquals(expectedPhoneNum, actual.iterator().next().getPhoneNumber());
    }
}