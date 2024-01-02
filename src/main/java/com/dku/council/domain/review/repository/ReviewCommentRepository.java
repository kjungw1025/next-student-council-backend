package com.dku.council.domain.review.repository;

import com.dku.council.domain.review.model.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    @Query(value = "select group_concat(r.review_number) from review_comment r " +
                    "join with_dankook_user w " +
                    "on w.with_dankook_user_id = r.with_dankook_user_id " +
                    "where r.with_dankook_type = :withDankookType and " +
                            "r.is_positive = true and " +
                            "w.participant_id = :userId ", nativeQuery = true)
    Optional<String> findAllPositiveReviewNumber(String withDankookType, Long userId);

    @Query(value = "select group_concat(r.review_number) from review_comment r " +
            "join with_dankook_user w " +
            "on w.with_dankook_user_id = r.with_dankook_user_id " +
            "where r.with_dankook_type = :withDankookType and " +
            "r.is_positive = false and " +
            "w.participant_id = :userId ", nativeQuery = true)
    Optional<String> findAllNegativeReviewNumber(String withDankookType, Long userId);
}
