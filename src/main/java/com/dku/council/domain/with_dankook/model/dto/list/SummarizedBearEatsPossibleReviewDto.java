package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class SummarizedBearEatsPossibleReviewDto {
    @NotNull
    @Schema(description = "게시글 id", example = "5")
    private final Long withDankookId;

    @Schema(description = "음식점 이름", example = "체리스시")
    private final String restaurant;

    @Schema(description = "배달 장소", example = "혜당관 406호")
    private final String deliveryPlace;

    @Schema(description = "배달 주문 시간", example = "2024-01-01 12:30:00")
    private final LocalDateTime deliveryTime;

    @Schema(description = "리뷰를 작성할 사용자들 리스트", example = "[1, 3, 4]")
    private final List<RecruitedUsersDto> targetUserList;

    public SummarizedBearEatsPossibleReviewDto(BearEats bearEats, Long writerId) {
        this.withDankookId = bearEats.getId();
        this.restaurant = bearEats.getRestaurant();
        this.deliveryPlace = bearEats.getDeliveryPlace();
        this.deliveryTime = bearEats.getDeliveryTime();
        this.targetUserList = bearEats.getUsers().stream()
                .filter(user -> !Objects.equals(user.getParticipant().getId(), writerId))
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
    }
}
