package com.dku.council.domain.admin.dto;

import com.dku.council.domain.banner.model.BannerImage;
import com.dku.council.domain.banner.model.entity.Banner;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.Getter;

@Getter
public class AdBannerPageDto {
    private final Long id;
    private final String bannerUrl;
    private final String redirectUrl;
    private final int viewCount;
    private final int distinctViewCount;
    private final int clickCount;

    public AdBannerPageDto(Banner banner, BannerImage image, ObjectUploadContext context) {
        this.id = banner.getId();
        this.bannerUrl = context.getImageUrl(image.getFileId());
        this.redirectUrl = banner.getBannerUrl();
        this.viewCount = banner.getViewCount();
        this.distinctViewCount = banner.getDistinctViewCount();
        this.clickCount = banner.getClickCount();
    }
}
