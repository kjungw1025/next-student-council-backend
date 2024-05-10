package com.dku.council.domain.banner.model.dto.response;

import com.dku.council.domain.banner.model.dto.BannerImageDto;
import com.dku.council.domain.banner.model.entity.Banner;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ResponseBannerDto {

    private final Long id;

    private final String bannerUrl;

    private final List<BannerImageDto> images;

    public ResponseBannerDto(Banner banner, ObjectUploadContext context) {
        this.id = banner.getId();
        this.bannerUrl = banner.getBannerUrl();
        this.images = BannerImageDto.listOf(context, banner.getImages());
    }
}
