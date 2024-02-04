package com.dku.council.infra.nhn.s3.model;

import com.dku.council.domain.chat.model.FileType;
import lombok.Getter;

@Getter
public class ChatUploadedImage {

    private final String roomId;

    private final Long userId;

    private final String fileName;

    private final String fileUrl;

    private final String fileType;

    public ChatUploadedImage(String roomId, Long userId, String fileUrl, ImageRequest image) {
        this.roomId = roomId;
        this.userId = userId;
        this.fileName = image.getOriginalFilename();
        this.fileUrl = fileUrl;
        this.fileType = FileType.IMAGE.toString();
    }
}
