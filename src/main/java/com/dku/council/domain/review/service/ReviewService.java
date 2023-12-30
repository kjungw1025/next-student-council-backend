package com.dku.council.domain.review.service;

import com.dku.council.domain.review.exception.ReviewNotFoundException;
import com.dku.council.domain.review.model.dto.request.RequestCreateReviewDto;
import com.dku.council.domain.review.model.dto.response.ResponseReviewPositiveCountDto;
import com.dku.council.domain.review.model.entity.Review;
import com.dku.council.domain.review.model.entity.ReviewComment;
import com.dku.council.domain.review.repository.ReviewCommentRepository;
import com.dku.council.domain.review.repository.ReviewRepository;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.WithDankookUserNotFoundException;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import com.dku.council.domain.with_dankook.repository.WithDankookRepository;
import com.dku.council.domain.with_dankook.repository.WithDankookUserRepository;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final WithDankookRepository<WithDankook> withDankookRepository;
    private final WithDankookUserRepository withDankookUserRepository;
    private final UserRepository userRepository;

    private final MessageSource messageSource;

    /**
     * With-Dankook 타입에 해당하는 리뷰 작성
     */
    @Transactional
    public void create(Long writerUserId, RequestCreateReviewDto dto) {
        User user = userRepository.findById(dto.getTargetUserId()).orElseThrow(UserNotFoundException::new);
        String withDankookType = withDankookRepository.findWithDankookType(dto.getWithDankookId());

        List<Review> reviewList = reviewRepository.findByUserId(withDankookType, dto.getTargetUserId());
        
        Review review;
        if (reviewList.isEmpty()) {
            // with-dankook type에 해당하는 게시판에 리뷰가 한번도 반영이 되지 않은 사용자라면, 새로 생성
            review = Review.builder()
                    .withDankookType(withDankookType)
                    .user(user)
                    .build();
            reviewRepository.save(review).getId();
        } else {
            // 한번 이상 리뷰가 반영된 사용자라면, 기존 정보를 사용
            review = reviewList.get(0);
        }
        
        // 리뷰를 작성한 사용자가 추천해요! / 아쉬워요! 중, 선택한 것에 따라 받은 개수 반영
        if (dto.getIsPositive()) {
            review.addPositiveCount(1);
        } else {
            review.addNegativeCount(1);
        }

        WithDankookUser withDankookTargetUser = withDankookUserRepository.isExistsByWithDankookIdAndUserId(dto.getWithDankookId(), dto.getTargetUserId())
                .orElseThrow(WithDankookUserNotFoundException::new);
        
        // 리뷰 상세 내용 저장
        for (int i = 0; i < dto.getReviewList().size(); i++) {
            ReviewComment reviewComment = ReviewComment.builder()
                    .withDankookType(withDankookType)
                    .withDankookUser(withDankookTargetUser)
                    .writerUserId(writerUserId)
                    .reviewNumber(dto.getReviewList().get(i))
                    .isPositive(dto.getIsPositive())
                    .build();
            reviewCommentRepository.save(reviewComment);
        }

        // with_dankook_user(writer)의 review_status를 true로 변경
        WithDankookUser withDankookWriterUser = withDankookUserRepository.isExistsByWithDankookIdAndUserId(dto.getWithDankookId(), writerUserId)
                .orElseThrow(WithDankookUserNotFoundException::new);
        withDankookWriterUser.changeReviewStatus();
        withDankookUserRepository.save(withDankookWriterUser);
    }

    /**
     * With-Dankook 타입 별, 추천해요 리뷰를 많이 받은 유저 순위 조회
     */
    public Page<ResponseReviewPositiveCountDto> rank(String withDankookType, Pageable pageable) {
        return reviewRepository.findPositiveUserRank(withDankookType, pageable)
                .map(ResponseReviewPositiveCountDto::new);
    }

    /**
     * With-Dankook 타입 별, 특정 사용자가 받은 모든 '추천해요!' 리뷰 내역 상세 조회
     */
    public Map<String, Integer> findAllUserPositiveReviewDetail(String withDankookType, Long userId) {
        String str = reviewCommentRepository.findAllPositiveReviewNumber(withDankookType, userId).orElseThrow(ReviewNotFoundException::new);
        int positiveCount = reviewRepository.findPositiveCount(withDankookType, userId);

        Map<String, Integer> result = countAndSaveReviewNumber(withDankookType, "positive", str);
        result.put("positiveCount", positiveCount);

        return result;
    }

    /**
     * With-Dankook 타입 별, 특정 사용자가 받은 모든 '아쉬워요!' 리뷰 내역 상세 조회
     */
    public Map<String, Integer> findAllUserNegativeReviewDetail(String withDankookType, Long userId) {
        String str = reviewCommentRepository.findAllNegativeReviewNumber(withDankookType, userId).orElseThrow(ReviewNotFoundException::new);
        int negativeCount = reviewRepository.findNegativeCount(withDankookType, userId);

        Map<String, Integer> result = countAndSaveReviewNumber(withDankookType, "negative", str);
        result.put("negativeCount", negativeCount);

        return result;
    }


    private Map<String, Integer> countAndSaveReviewNumber(String withDankookType, String flag, String input) {
        Locale locale = LocaleContextHolder.getLocale();

        // 공백 및 쉼표를 제거하고 숫자만 남기기
        String[] numbers = input.replaceAll("\\s", "").split(",");

        // 각 숫자의 등장 횟수를 저장할 Map
        Map<String, Integer> countMap = new HashMap<>();

        String keyStr = "withdankook.review." + withDankookType.toLowerCase() + "." + flag + ".";
        // 각 숫자의 등장 횟수를 세기
        for (String number : numbers) {

            String key = messageSource.getMessage(keyStr + number, null, locale);

            // HashMap에 이미 해당 숫자가 있는 경우 기존 값에서 1 증가
            // 없는 경우 1로 초기화
            countMap.put(key, countMap.getOrDefault(key, 0) + 1);
        }

        return countMap;
    }
}
