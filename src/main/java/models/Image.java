package models;

import java.util.Objects;

public class Image {
    private final String phoneNumber;
    private final String text;
    private final String date;
    private final String imageUrl;

    private Image(String imageUrl, RedactedImageMetadata redactedImageMetadata) {
        this.imageUrl = imageUrl;
        this.phoneNumber = redactedImageMetadata.getPhoneNumber();
        this.text = redactedImageMetadata.getText();
        this.date = redactedImageMetadata.getDate();
    }

    public static Image fromMetadata(String imageUrl, RedactedImageMetadata redactedImageMetadata) {
        return new Image(imageUrl, redactedImageMetadata);
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(getPhoneNumber(), image.getPhoneNumber()) &&
                Objects.equals(getText(), image.getText()) &&
                Objects.equals(getDate(), image.getDate()) &&
                Objects.equals(getImageUrl(), image.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhoneNumber(), getText(), getDate(), getImageUrl());
    }
}
