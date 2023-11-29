package com.dku.council.infra.nhn.s3.model;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public class UploadedImage {
    private final String imageId;

    private final String originalName;

    private final MediaType mimeType;

    private final ImageRequest image;

    public UploadedImage(String imageId, ImageRequest image) {
        this.imageId = imageId;
        this.originalName = image.getOriginalFilename();
        this.mimeType = image.getContentType();
        this.image = image;
    }
}
