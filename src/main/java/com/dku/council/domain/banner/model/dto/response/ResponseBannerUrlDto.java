package com.dku.council.domain.banner.model.dto.response;

import lombok.Getter;

@Getter
public class ResponseBannerUrlDto {

    private final String url;

    public ResponseBannerUrlDto(String url) {
        this.url = url;
    }
}
