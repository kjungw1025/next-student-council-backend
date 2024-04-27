package com.dku.council.domain.banner.repository;

public interface BannerResetMemoryRepository {

    /**
     * 하루 한 번 조회한 사용자 목록 초기화
     *
     * @param bannerId      조회한 배너 ID
     */
    void clearAllDistinct(Long bannerId);
}
