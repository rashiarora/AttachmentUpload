package com.rashi.attachmentsupload;

/**
 * Created by Admin on 02-05-2017.
 */

public class Bean {
    String encodedImage, filePath;

    public Bean() {
    }

    public Bean(String encodedImage, String filePath) {
        this.encodedImage = encodedImage;
        this.filePath = filePath;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "encodedImage='" + encodedImage + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
