package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.ResidenceDuration;
import com.dku.council.domain.with_dankook.model.entity.type.Roommate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class SummarizedRoommatePossibleReviewDto {
    @NotNull
    @Schema(description = "게시글 id", example = "5")
    private final Long withDankookId;

    @NotNull
    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @NotNull
    @Schema(description = "생활관", example = "웅비홀")
    private final String livingPlace;

    @Schema(description = "기숙사 입사 기간", example = "SEMESTER")
    private final ResidenceDuration residenceDuration;

    @Schema(description = "리뷰를 작성할 사용자들 리스트", example = "[1, 3, 4]")
    private final List<RecruitedUsersDto> targetUserList;

    public SummarizedRoommatePossibleReviewDto(Roommate roommate, Long writerId) {
        this.withDankookId = roommate.getId();
        this.title = roommate.getTitle();
        this.livingPlace = roommate.getLivingPlace();
        this.residenceDuration = roommate.getResidenceDuration();
        this.targetUserList = roommate.getUsers().stream()
                .filter(user -> !Objects.equals(user.getParticipant().getId(), writerId))
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
    }
}
