package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.with_dankook.repository.WithDankookUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WithDankookUserService {
    private final WithDankookUserRepository withDankookUserRepository;

    /**
     * With-Dankook 특정 게시판의 모집된 인원 수 조회
     *
     * @param withDankookId     조회할 게시글 id
     * @return                  모집된 인원 수
     */
    public int recruitedCount(Long withDankookId) {
        return withDankookUserRepository.findRecruitedById(withDankookId);
    }
}
