package com.dku.council.domain.review.model.dto.response;

import com.dku.council.domain.review.model.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ResponseReviewPositiveCountDto {
    @Schema(description = "유저 닉네임", example="Leonardo DiCaprio")
    private final String userName;

    @Schema(description = "추천해요 개수", example="100")
    private final int positiveCount;

    public ResponseReviewPositiveCountDto(Review review) {
        this.userName = review.getUser().getNickname();
        this.positiveCount = review.getPositiveCount();
    }
}
