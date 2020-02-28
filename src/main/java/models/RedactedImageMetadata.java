package models;

public class RedactedImageMetadata {
    private final String username;
    private final String imageId;
    private final String text;
    private final String date;

    public RedactedImageMetadata(String username, String imageId, String text, String date) {
        this.username = username;
        this.imageId = imageId;
        this.text = text;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getImageId() {
        return imageId;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
