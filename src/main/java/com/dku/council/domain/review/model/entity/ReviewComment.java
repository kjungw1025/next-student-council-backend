package com.dku.council.domain.review.model.entity;

import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewComment {
    @Id
    @GeneratedValue
    @Column(name = "review_comment_id")
    private Long id;

    @NotBlank
    private String withDankookType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "with_dankook_user_id")
    private WithDankookUser withDankookUser;

    @NotNull
    private Long writerUserId;

    @NotNull
    private int reviewNumber;

    private boolean isPositive;

    @Builder
    private ReviewComment(String withDankookType,
                          WithDankookUser withDankookUser,
                          Long writerUserId,
                          int reviewNumber,
                          boolean isPositive) {
        this.withDankookType = withDankookType;
        this.withDankookUser = withDankookUser;
        this.writerUserId = writerUserId;
        this.reviewNumber = reviewNumber;
        this.isPositive = isPositive;
    }
}
