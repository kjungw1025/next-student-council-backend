package com.dku.council.infra.nhn.s3.model;

import com.dku.council.domain.chat.model.FileType;
import lombok.Getter;

@Getter
public class ChatUploadedFile {
    private final String roomId;

    private final Long userId;

    private final String fileName;

    private final String fileUrl;

    private final String fileType;

    public ChatUploadedFile(String roomId, Long userId, String fileUrl, FileRequest file) {
        this.roomId = roomId;
        this.userId = userId;
        this.fileName = file.getOriginalFilename();
        this.fileUrl = fileUrl;
        this.fileType = FileType.FILE.toString();
    }
}
