package com.dku.council.infra.nhn.s3.model;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public class OriginalUploadedImage {
    private final String fileId;

    private final String originalName;

    private final MediaType mimeType;

    private final ImageRequest file;

    public OriginalUploadedImage(String fileId, ImageRequest file) {
        this.fileId = fileId;
        this.originalName = file.getOriginalFilename();
        this.mimeType = file.getContentType();
        this.file = file;
    }
}
