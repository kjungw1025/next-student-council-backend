package com.dku.council.domain.banner.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class RequestCreateBannerDto {
    private final String bannerUrl;

    private final MultipartFile image;

    private final String startDate;

    private final String endDate;

}
