package services;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import models.Image;
import models.RedactedImageMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repositories.DynamoReader;
import repositories.S3Reader;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageRetrievalServiceTest {

    @Mock
    private DynamoReader dynamoReader;
    @Mock
    private S3Reader s3Reader;
    @InjectMocks
    private ImageRetrievalService imageRetrievalService;

    @Test
    public void retrieveAllImages_GivenDataInDynamoAndS3_ReturnImage() {
        // given
        Map<String, AttributeValue> attributeMap = Map.of(
                "phone-number", new AttributeValue().withS("+15555555555"),
                "objectKey",  new AttributeValue().withS("1"),
                "text",  new AttributeValue().withS("text-itself"),
                "date",  new AttributeValue().withN("1582915550569")
        );
        Map<String, AttributeValue> attributeMap2 = Map.of(
                "phone-number", new AttributeValue().withS("+12222222222"),
                "objectKey",  new AttributeValue().withS("2"),
                "text",  new AttributeValue().withS("text2"),
                "date",  new AttributeValue().withN("1582915550569")
        );
        RedactedImageMetadata metadata1 = RedactedImageMetadata.fromDbEntity(attributeMap);
        RedactedImageMetadata metadata2 = RedactedImageMetadata.fromDbEntity(attributeMap2);
        String imageUrl1 = "fakeUrl1";
        String imageUrl2 = "fakeUrl2";
        Set<Image> expected = Set.of(Image.fromMetadata(imageUrl1, metadata1), Image.fromMetadata(imageUrl2, metadata2));

        // then
        when(dynamoReader.retrieveAllImageMetadata()).thenReturn(Set.of(metadata1, metadata2));
        when(s3Reader.retrieveImageUrl("1")).thenReturn(imageUrl1);
        when(s3Reader.retrieveImageUrl("2")).thenReturn(imageUrl2);

        Set<Image> actual = imageRetrievalService.retrieveAllImages();

        // when
        verify(dynamoReader, times(1)).retrieveAllImageMetadata();
        verify(s3Reader, times(1)).retrieveImageUrl(metadata1.getObjectKey());
        verify(s3Reader, times(1)).retrieveImageUrl(metadata2.getObjectKey());

        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }
}