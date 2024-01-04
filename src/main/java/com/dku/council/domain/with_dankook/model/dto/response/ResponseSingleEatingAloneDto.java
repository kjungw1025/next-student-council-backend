package com.dku.council.domain.with_dankook.model.dto.response;

import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.type.EatingAlone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResponseSingleEatingAloneDto extends ResponseSingleWithDankookDto {

    @Schema(description = "제목", example = "피자헛에서 피자를 시켜먹을 사람을 구합니다.")
    private final String title;

    @Schema(description = "내용", example = "학교 앞에서 먹을겁니다.")
    private final String content;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    @Schema(description = "모집된 사용자들")
    private final List<RecruitedUsersDto> recruitedUsers;

    public ResponseSingleEatingAloneDto(ResponseSingleWithDankookDto dto, EatingAlone eatingAlone, int recruitedCount) {
        super(dto);
        this.title = eatingAlone.getTitle();
        this.content = eatingAlone.getContent();
        this.recruitedCount = recruitedCount;
        this.recruitedUsers = eatingAlone.getUsers().stream()
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
    }
}
