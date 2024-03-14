package com.dku.council.domain.danfesta.model.dto;

import com.dku.council.domain.danfesta.model.entity.LineUpImage;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class LineUpImageDto {

    @Schema(description = "이미지 url", example = "http://1.2.3.4./weifowirgoifof-rigeor")
    private final String url;

    @Schema(description = "원본이미지 파일 이름", example = "my_image.png")
    private final String originalName;

    @Schema(description = "이미지 파일 타입", example = "image/jpeg")
    private final String mimeType;

    public LineUpImageDto(ObjectUploadContext context, LineUpImage image) {
        this.url = context.getImageUrl(image.getFileId());
        this.originalName = image.getFileName();

        String imageMimeType = image.getMimeType();
        this.mimeType = Objects.requireNonNullElse(imageMimeType, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    public static List<LineUpImageDto> listOf(ObjectUploadContext context, List<LineUpImage> entities) {
        List<LineUpImage> result = new ArrayList<>(entities);

        return result.stream()
                .map(image -> new LineUpImageDto(context, image))
                .collect(Collectors.toList());
    }
}
