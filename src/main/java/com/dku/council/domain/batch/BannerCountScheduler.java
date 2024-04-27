package com.dku.council.domain.batch;

import com.dku.council.domain.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class BannerCountScheduler {

    private final BannerService bannerService;

    @Scheduled(cron = "${app.banner.reset-cron}")
    public void resetBannerDistinctCount() {
        bannerService.resetBannerDistinctCount();
    }
}
