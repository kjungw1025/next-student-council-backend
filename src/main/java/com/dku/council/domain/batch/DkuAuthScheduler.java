package com.dku.council.domain.batch;

import com.dku.council.infra.dku.service.DkuAuthBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class DkuAuthScheduler {

    private final DkuAuthBatchService dkuAuthBatchService;
    @Scheduled(cron = "${dku.auth.cron}")
    public void dkuAuthScheduler() {
        dkuAuthBatchService.resetDkuAuth();
    }
}
