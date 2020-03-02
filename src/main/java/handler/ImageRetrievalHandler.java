package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Image;
import services.ImageRetrievalService;

import java.util.Map;
import java.util.Set;

public class ImageRetrievalHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ImageRetrievalService imageRetrievalService;

    public ImageRetrievalHandler() {
        this(new ImageRetrievalService());
    }

    public ImageRetrievalHandler(ImageRetrievalService imageRetrievalService) {
        this.imageRetrievalService = imageRetrievalService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            Set<Image> images = imageRetrievalService.retrieveAllImages();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(new ObjectMapper().writeValueAsString(images));
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Failed to serialize images to JSON string.");
        }
    }
}
