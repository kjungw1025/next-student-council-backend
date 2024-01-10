package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.type.EatingAlone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class SummarizedEatingAlonePossibleReviewDto {
    @NotNull
    @Schema(description = "게시글 id", example = "5")
    private final Long withDankookId;

    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @Schema(description = "내용", example = "게시글 본문")
    private final String content;

    @Schema(description = "리뷰를 작성할 사용자들 리스트", example = "[1, 3, 4]")
    private final List<RecruitedUsersDto> targetUserList;

    public SummarizedEatingAlonePossibleReviewDto(EatingAlone eatingAlone, Long writerId) {
        this.withDankookId = eatingAlone.getId();
        this.title = eatingAlone.getTitle();
        this.content = eatingAlone.getContent();
        this.targetUserList = eatingAlone.getUsers().stream()
                .filter(user -> !Objects.equals(user.getParticipant().getId(), writerId))
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
    }
}
