package repositories;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Reader {

    private static final Regions REGION = Regions.US_EAST_2;
    private final String bucketName;
    private final AmazonS3 s3Client;

    public S3Reader() {
        this(AmazonS3ClientBuilder.standard().withRegion(REGION).build(), System.getenv("BUCKET_NAME"));
    }

    public S3Reader(AmazonS3 s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String retrieveImageUrl(String imageId) {
        return s3Client.getUrl(bucketName, imageId).toString();
    }
}
