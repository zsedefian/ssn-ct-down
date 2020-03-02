package services;

import models.Image;
import models.RedactedImageMetadata;
import repositories.DynamoReader;
import repositories.S3Reader;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Retrieves images from AWS storage.
 */
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

    /**
     * Retrieves all meta data from DynamoDB, then uses the retrieved object key to look up the URL of the image itself
     * in an S3 bucket.
     *
     * @return Set of all {@link Image} objects that are contained in the given S3 bucket
     */
    public Set<Image> retrieveAllImages() {
        return dynamoReader.retrieveAllImageMetadata()
                .stream()
                .map(this::createImage)
                .collect(Collectors.toSet());
    }

    private Image createImage(RedactedImageMetadata imageMetadata) {
        return Image.fromMetadata(s3Reader.retrieveImageUrl(imageMetadata.getObjectKey()), imageMetadata);
    }
}
