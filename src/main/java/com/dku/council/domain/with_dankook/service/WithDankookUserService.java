package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import com.dku.council.domain.with_dankook.repository.WithDankookUserRepository;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    /**
     * With-Dankook 게시판에 참여한 유저인지 확인합니다.
     *
     * @param withDankookId   게시글 id
     * @param userId          유저 id
     * @return                참여한 유저인지 여부
     */
    public boolean isParticipant(Long withDankookId, Long userId) {
        return withDankookUserRepository.isExistsByWithDankookIdAndUserId(withDankookId, userId).isPresent();
    }

    public boolean isPossibleWriteReview(Long withDankookId, Long userId) {
        return withDankookUserRepository.checkReviewStatus(withDankookId, userId).isPresent();
    }
}
