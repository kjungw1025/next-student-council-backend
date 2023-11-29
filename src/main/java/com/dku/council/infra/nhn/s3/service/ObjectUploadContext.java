package com.dku.council.infra.nhn.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ObjectUploadContext {

    //TODO 현재 존재하는 이미지 파일로 인해서 추후에 수정 예정
    @Value("${nhn.os.api-path}")
    private final String apiPath;

    @Value("${nhn.os.api-image-path}")
    private final String apiImagePath;

    @Value("${nhn.os.api-file-path}")
    private final String apiFilePath;

    @Value("${app.post.thumbnail.default}")
    private final String defaultThumbnailId;


    public String getThumbnailUrl(String thumbnailId) {
        if (thumbnailId == null || thumbnailId.isBlank()) {
            return getObjectUrl(defaultThumbnailId);
        } else {
            return getObjectUrl(thumbnailId);
        }
    }

    public String getObjectUrl(String objectName) {
        return String.format(apiPath, objectName);
    }

    public String getImageUrl(String objectName) {
        return String.format(apiImagePath, objectName);
    }

    public String getFileUrl(String objectName) {
        return String.format(apiFilePath, objectName);
    }

    public String makeObjectId(String prefix, String extension) {
        return makeObjName(prefix, UUID.randomUUID() + "." + extension);
    }

    public String makeImageId(String prefix, String extension) {
        return makeObjName(prefix, UUID.randomUUID() + "." + extension);
    }

    public String makeFileId(String prefix, String extension) {
        return makeObjName(prefix, UUID.randomUUID() + "." + extension);
    }

    private String makeObjName(String prefix, String id) {
        return prefix + "-" + id;
    }
}
