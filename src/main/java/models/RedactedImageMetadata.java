package models;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Partially models DynamoDB {@code ssn-ct-table} row.
 */
public class RedactedImageMetadata {
    private String phoneNumber;
    private String objectKey;
    private String text;
    private String date;

    public static RedactedImageMetadata fromDbEntity(Map<String, AttributeValue> attributeValueMap) {
        return new RedactedImageMetadata(
                attributeValueMap.get("phone-number").getS(),
                attributeValueMap.get("objectKey").getS(),
                attributeValueMap.get("text").getS(),
                DateFormat.getDateTimeInstance()
                        .format(new Date(Long.parseLong(attributeValueMap.get("date").getN())))
        );
    }

    private RedactedImageMetadata(String phoneNumber, String objectKey, String text, String date) {
        this.phoneNumber = phoneNumber;
        this.objectKey = objectKey;
        this.text = text;
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
