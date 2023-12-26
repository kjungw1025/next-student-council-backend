package com.dku.council.domain.with_dankook.model.dto.request;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class RequestCreateBearEatsDto extends RequestCreateWithDankookDto<BearEats>{

    @NotNull
    @Schema(description = "음식점", example = "피자헛")
    private String restaurant;

    @NotNull
    @Schema(description = "배달 주문 장소", example = "피자헛")
    private String deliveryPlace;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM")
    @Schema(description = "배달 시간", example = "2023-12-25 17:30")
    private LocalDateTime deliveryTime;

    @NotBlank
    @Schema(description = "본문", example = "내용")
    private String content;

    public RequestCreateBearEatsDto(@NotNull String restaurant,
                                    @NotNull String deliveryPlace,
                                    @NotNull LocalDateTime deliveryTime,
                                    @NotBlank String content) {
        this.restaurant = restaurant;
        this.deliveryPlace = deliveryPlace;
        this.deliveryTime = deliveryTime;
        this.content = content;
    }

    @Override
    public BearEats toEntity(User user) {
        return BearEats.builder()
                .user(user)
                .restaurant(restaurant)
                .deliveryPlace(deliveryPlace)
                .deliveryTime(deliveryTime)
                .content(content)
                .build();
    }
}
