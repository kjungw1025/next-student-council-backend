package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SummarizedBearEatsDto extends SummarizedWithDankookDto {

    @Schema(description = "식당 이름", example = "피자헛")
    private final String restaurant;

    @Schema(description = "배달 주문 장소", example = "피자헛")
    private final String deliveryPlace;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "배달 시간", example = "2021-01-01T12:00")
    private final LocalDateTime deliveryTime;

    @Schema(description = "내용", example = "피자헛에서 피자를 시켜먹을 사람을 구합니다.")
    private final String content;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    public SummarizedBearEatsDto(SummarizedWithDankookDto dto, BearEats bearEats, int recruitedCount) {
        super(dto);
        this.restaurant = bearEats.getRestaurant();
        this.deliveryPlace = bearEats.getDeliveryPlace();
        this.deliveryTime = bearEats.getDeliveryTime();
        this.content = bearEats.getContent();
        this.recruitedCount = recruitedCount;
    }

}
