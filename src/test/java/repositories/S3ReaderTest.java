package repositories;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class S3ReaderTest {

    @Mock
    private AmazonS3 mockS3Client;

    private S3Reader s3Reader;
    private String bucketName = "EXAMPLE_BUCKET";

    @Before
    public void setup() {
        s3Reader = new S3Reader(mockS3Client, bucketName);
    }

    @Test
    public void retrieveImageUrl() {
        // given
        try {
            String objectKey = "key";
            String urlString = "https://stackoverflow.com/";
            URL url = new URL(urlString);

            // when
            when(mockS3Client.getUrl(bucketName, objectKey)).thenReturn(url);

            String imageUrl = s3Reader.retrieveImageUrl(objectKey);

            // then
            verify(mockS3Client, times(1)).getUrl(bucketName, objectKey);
            assertNotNull(imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}