package com.dku.council.domain.with_dankook.repository.with_dankook;

import java.time.Duration;
import java.time.Instant;

public interface WithDankookMemoryRepository {

    /**
     * 단국거래 글 작성 시 존재하는지 확인한다.
     *
     * @param withDankookType With-Dankook 타입
     * @param userId          유저 ID
     * @param now             현재 시각
     * @return 이미 존재하면 true, 아니면 false반환.
     */
    boolean isAlreadyContains(String withDankookType, Long userId, Instant now);

    /**
     * 단국거래 글 작성 시 새로 추가한다. 추가하고나서 expiresAfter분 뒤에 삭제된다.
     *
     * @param withDankookType With-Dankook 타입
     * @param userId          유저 ID
     * @param expiresAfter    캐시 유지시간.
     * @param now             현재 시각
     */
    void put(String withDankookType, Long userId, Duration expiresAfter, Instant now);
}
