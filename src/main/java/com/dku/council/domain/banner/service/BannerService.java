package com.dku.council.domain.banner.service;

import com.dku.council.domain.banner.exception.BannerNotFoundException;
import com.dku.council.domain.banner.model.dto.request.RequestCreateBannerDto;
import com.dku.council.domain.banner.model.dto.response.ResponseBannerDto;
import com.dku.council.domain.banner.model.entity.Banner;
import com.dku.council.domain.banner.model.entity.BannerCount;
import com.dku.council.domain.banner.repository.BannerCountRepository;
import com.dku.council.domain.banner.repository.BannerMemoryRepository;
import com.dku.council.domain.banner.repository.BannerRepository;
import com.dku.council.domain.banner.repository.BannerResetMemoryRepository;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository persistRepository;
    private final BannerMemoryRepository memoryRepository;
    private final BannerResetMemoryRepository resetMemoryRepository;
    private final BannerCountRepository bannerCountRepository;

    private final ObjectUploadContext objectUploadContext;
    private final Clock clock;

    @Value("${app.banner.count-cooltime}")
    private final Duration countCoolTime;

    @Value("${app.banner.distinct-cooltime}")
    private final Duration distinctCoolTime;

    @Value("${app.banner.click-cooltime}")
    private final Duration clickCoolTime;

    /**
     * 배너 전체 조회
     *
     * @param remoteAddress   요청자 IP 주소
     */
    @Transactional
    public List<ResponseBannerDto> getBannerList(String remoteAddress) {
        Instant now = Instant.now(clock);
        List<Banner> banners = persistRepository.findAll();

        for(Banner banner : banners) {
            // 하루 한번 제한하는 로직
            if(!memoryRepository.existDistinct(banner.getId(), remoteAddress, now)) {
                memoryRepository.putDistinct(banner.getId(), remoteAddress, distinctCoolTime, now);
                banner.addViewDistinctCount();
            }
            // 중복 포함해서 3분이 안 지났는지 확인하는 로직
            if(!memoryRepository.existCount(banner.getId(), remoteAddress, now)) {
                memoryRepository.putCount(banner.getId(), remoteAddress, countCoolTime, now);
                banner.addViewCount();
            }
        }

        return banners.stream()
                .map(banner -> new ResponseBannerDto(banner, objectUploadContext))
                .collect(Collectors.toList());
    }

    /**
     * 배너 Redirect URL 조회
     *
     * @param bannerId   배너 ID
     * @return 배너 URL
     */
    @Transactional
    public String getBannerUrl(Long bannerId, String remoteAddress) {
        Instant now = Instant.now(clock);
        Banner banner = persistRepository.findById(bannerId)
                .orElseThrow(BannerNotFoundException::new);
        if(!memoryRepository.existClick(bannerId, remoteAddress, now)) {
            memoryRepository.putClick(bannerId, remoteAddress, clickCoolTime, now);
            banner.addClickCount();
        }
        return banner.getBannerUrl();
    }

    /**
     * IP별 배너 조회 매일 00시에 초기화
     *
     */
    @Transactional
    public void resetBannerDistinctCount() {
        List<Banner> all = persistRepository.findAll();
        for(Banner banner : all) {
            resetMemoryRepository.clearAllDistinct(banner.getId());
            int dc = banner.getDistinctViewCount();
            BannerCount bc = BannerCount.builder()
                    .banner(banner)
                    .distinctViewCount(dc)
                    .dailyDistinctViewDate(LocalDateTime.now().minusDays(1).withSecond(0).withHour(0).withMinute(0))
                    .build();
            bannerCountRepository.save(bc);
            banner.resetDistinctViewCount();
        }
    }
}
