package com.dku.council.domain.review.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class RequestCreateReviewDto {
    @NotNull
    @Schema(description = "게시글 id", example = "5")
    private final Long withDankookId;

    @NotNull
    @Schema(description = "리뷰를 받는 유저 id", example = "35")
    private final Long targetUserId;

    @Schema(description = "추천해요! = true / 아쉬워요! = false", example = "true")
    private final Boolean isPositive;

    @Schema(description = "리뷰내용 번호 리스트", example = "[1, 3, 4]")
    private final List<Integer> reviewList;

    public RequestCreateReviewDto(@NotBlank Long withDankookId,
                                  @NotBlank Long targetUserId,
                                  Boolean isPositive,
                                  @NotBlank List<Integer> reviewList) {
        this.withDankookId = withDankookId;
        this.targetUserId = targetUserId;
        this.isPositive = isPositive;
        this.reviewList = reviewList;
    }
}
