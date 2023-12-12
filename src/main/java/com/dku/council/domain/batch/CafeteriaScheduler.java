package com.dku.council.domain.batch;

import com.dku.council.domain.cafeteria.service.CafeteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class CafeteriaScheduler {

    private final CafeteriaService cafeteriaService;
    @Scheduled(cron = "${dku.cafeteria.cron}")
    public void loadToDB() {
        cafeteriaService.loadCafeteria();
    }
}
