package services;

import models.Image;
import repositories.DynamoReader;
import repositories.S3Reader;

import java.util.Set;
import java.util.stream.Collectors;

public class ImageRetrievalService {

    private final DynamoReader dynamoReader;
    private final S3Reader s3Reader;

    public ImageRetrievalService() {
        this(new DynamoReader(), new S3Reader());
    }

    public ImageRetrievalService(DynamoReader dynamoReader, S3Reader s3Reader) {
        this.dynamoReader = dynamoReader;
        this.s3Reader = s3Reader;
    }

    public Set<Image> retrieveAllImages() {
        return dynamoReader.retrieveAllImageMetadata().stream()
                .map(imageMetadata -> new Image(s3Reader.retrieveImageUrl(imageMetadata.getImageId()), imageMetadata))
                .collect(Collectors.toSet());
    }
}
