package com.dku.council.domain.banner.model.dto;

import com.dku.council.domain.banner.model.BannerImage;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class BannerImageDto {

    private final Long id;

    private final String url;

    private final String originalName;

    private final String mimeType;

    public BannerImageDto(ObjectUploadContext context, BannerImage image) {
        this.id = image.getId();
        this.url = context.getImageUrl(image.getFileId());
        this.originalName = image.getFileName();

        String imageMimeType = image.getMimeType();
        this.mimeType = Objects.requireNonNullElse(imageMimeType, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    public static List<BannerImageDto> listOf(ObjectUploadContext context, List<BannerImage> entities) {
        List<BannerImage> result = new ArrayList<>();

        for (BannerImage entity : entities) {
            result.add(entity);
        }
        return result.stream()
                .map(image -> new BannerImageDto(context, image))
                .collect(Collectors.toList());
    }
}
