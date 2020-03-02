package models;

public class Image {
    private final String username;
    private final String text;
    private final String date;
    private final String imageUrl;

    private Image(String imageUrl, RedactedImageMetadata redactedImageMetadata) {
        this.imageUrl = imageUrl;
        this.username = redactedImageMetadata.getUsername();
        this.text = redactedImageMetadata.getText();
        this.date = redactedImageMetadata.getDate();
    }

    public static Image fromMetadata(String imageUrl, RedactedImageMetadata redactedImageMetadata) {
        return new Image(imageUrl, redactedImageMetadata);
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
