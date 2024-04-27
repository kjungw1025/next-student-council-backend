package com.dku.council.domain.banner.repository;

import java.time.Duration;
import java.time.Instant;

public interface BannerMemoryRepository {

    /**
     * 배너 조회 시 조회수 증가 (중복 X, 하루 한 번)
     * 하루로 기간을 설정해놓고 00시에 초기화
     *
     * @param bannerId      조회한 배너 ID
     * @param remoteAddress 요청자 IP 주소
     * @param expiresAfter  캐시 유지시간
     * @param now           현재 시각
     */
    void putDistinct(Long bannerId, String remoteAddress, Duration expiresAfter, Instant now);

    /**
     * 배너 조회 시 조회수 증가 (중복 O, 3분 제한)
     *
     * @param bannerId      조회한 배너 ID
     * @param remoteAddress 요청자 IP 주소
     * @param expiresAfter  캐시 유지시간
     * @param now           현재 시각
     */
    void putCount(Long bannerId, String remoteAddress, Duration expiresAfter, Instant now);

    /**
     * 배너 클릭 시 클릭수 증가(중복 O, 1분 제한)
     *
     * @param bannerId      클릭한 배너 ID
     * @param remoteAddress 요청자 IP 주소
     * @param expiresAfter    캐시 유지시간
     * @param now             현재 시각
     */
    void putClick(Long bannerId, String remoteAddress, Duration expiresAfter, Instant now);

    /**
     * 하루 한 번 조회한 사용자인지 확인
     *
     * @param bannerId      조회한 배너 ID
     * @param remoteAddress 요청자 IP 주소
     */
    boolean existDistinct(Long bannerId, String remoteAddress, Instant now);

    /**
     * 3분 이내에 조회한 사용자인지 확인
     *
     * @param bannerId      조회한 배너 ID
     * @param remoteAddress 요청자 IP 주소
     */
    boolean existCount(Long bannerId, String remoteAddress, Instant now);

    /**
     * 1분 이내에 클릭한 사용자인지 확인
     *
     * @param bannerId      클릭한 배너 ID
     * @param remoteAddress 요청자 IP 주소
     */
    boolean existClick(Long bannerId, String remoteAddress, Instant now);
}
