package repositories;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

public class S3Reader {
    private static final Regions REGION = Regions.US_EAST_2;
    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    private final AmazonS3 s3Client;

    public S3Reader() {
        this(AmazonS3ClientBuilder.standard().withRegion(REGION).build());
    }

    public S3Reader(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String retrieveImageUrl(String imageId) {
        return s3Client.getObject(BUCKET_NAME, imageId).getKey();
    }
}
