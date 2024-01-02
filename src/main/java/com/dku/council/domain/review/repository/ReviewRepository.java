package com.dku.council.domain.review.repository;

import com.dku.council.domain.review.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r " +
            "where r.user.id = :userId and r.withDankookType = :withDankookType ")
    List<Review> findByUserId(String withDankookType, Long userId);

    @Query("select r.positiveCount from Review r " +
            "where r.user.id = :userId and r.withDankookType = :withDankookType ")
    int findPositiveCount(String withDankookType, Long userId);

    @Query("select r.negativeCount from Review r " +
            "where r.user.id = :userId and r.withDankookType = :withDankookType ")
    int findNegativeCount(String withDankookType, Long userId);

    @Query("select r from Review r " +
            "where r.withDankookType = :withDankookType " +
            "order by r.positiveCount desc")
    Page<Review> findPositiveUserRank(@Param("withDankookType") String withDankookType, Pageable pageable);

}
