package com.dku.council.domain.review.model.entity;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @NotBlank
    private String withDankookType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private int positiveCount;

    @NotNull
    private int negativeCount;

    @Builder
    private Review(String withDankookType,
                   User user) {
        this.withDankookType = withDankookType;
        this.user = user;
        this.positiveCount = 0;
        this.negativeCount = 0;
    }

    public void addPositiveCount(int value) {
        this.positiveCount += value;
    }

    public void addNegativeCount(int value) {
        this.negativeCount += value;
    }
}
