package handler;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import models.Image;
import models.RedactedImageMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import services.ImageRetrievalService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageRetrievalHandlerTest {

    @Mock
    ImageRetrievalService imageRetrievalService;
    @InjectMocks
    ImageRetrievalHandler imageRetrievalHandler;

    @Test
    public void handleRequest_GivenRequest_ReturnJsonString() {
        // given
        Map<String, AttributeValue> attributeMap = Map.of(
                "phone-number", new AttributeValue().withS("+15555555555"),
                "objectKey",  new AttributeValue().withS("1"),
                "text",  new AttributeValue().withS("text-itself"),
                "date",  new AttributeValue().withN("1582906773023")
        );
        RedactedImageMetadata metadata = RedactedImageMetadata.fromDbEntity(attributeMap);
        Set<Image> images = Set.of(Image.fromMetadata("fakeUrl", metadata));
        APIGatewayProxyRequestEvent expectedInput = new APIGatewayProxyRequestEvent();

        // when
        when(imageRetrievalService.retrieveAllImages()).thenReturn(images);
        APIGatewayProxyResponseEvent response = imageRetrievalHandler.handleRequest(expectedInput, null);

        // then
        verify(imageRetrievalService, times(1)).retrieveAllImages();
        assertNotNull(response.getBody());
    }
}