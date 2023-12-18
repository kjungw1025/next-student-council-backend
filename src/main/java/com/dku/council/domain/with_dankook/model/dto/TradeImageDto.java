package com.dku.council.domain.with_dankook.model.dto;

import com.dku.council.domain.with_dankook.model.entity.TradeImage;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TradeImageDto {

    @Schema(description = "이미지 아이디", example = "1")
    private final Long id;

    @Schema(description = "이미지 url", example = "http://1.2.3.4/1ddee68d-6afb-48d0-9cb6-04a8d8fea4ae.png")
    private final String url;

    @Schema(description = "썸네일 이미지 url (없으면 기본 이미지)", example = "http://1.2.3.4/thumb-1ddee68d-6afb-48d0-9cb6-04a8d8fea4ae.png")
    private final String thumbnailUrl;

    @Schema(description = "원본이미지 파일 이름", example = "my_image.png")
    private final String originalName;

    @Schema(description = "이미지 파일 타입", example = "image/jpeg")
    private final String mimeType;

    public TradeImageDto(ObjectUploadContext context, TradeImage image) {
        this.id = image.getId();
        this.url = context.getImageUrl(image.getFileId());
        this.thumbnailUrl = context.getThumbnailUrl(image.getThumbnailId());
        this.originalName = image.getFileName();

        String fileMimeType = image.getMimeType();
        this.mimeType = Objects.requireNonNullElse(fileMimeType, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    public static List<TradeImageDto> listOf(ObjectUploadContext context, List<TradeImage> entities) {
        List<TradeImage> result = new ArrayList<>();

        for (TradeImage entity : entities) {
            if (entity.getThumbnailId() != null) {
                result.add(entity);
            }
        }
        return result.stream()
                .map(image -> new TradeImageDto(context, image))
                .collect(Collectors.toList());
    }
}
