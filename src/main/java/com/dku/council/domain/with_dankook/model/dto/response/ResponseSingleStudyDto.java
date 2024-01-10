package com.dku.council.domain.with_dankook.model.dto.response;

import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.type.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResponseSingleStudyDto extends ResponseSingleWithDankookDto {

    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @Schema(description = "최소 학번", example = "19")
    private final int minStudentId;

    @Schema(description = "스터디 시작 시간", example = "2023-12-25 17:30:00")
    private final LocalDateTime startTime;

    @Schema(description = "스터디 끝나는 시간", example = "2023-12-25 18:30:00")
    private final LocalDateTime endTime;

    @Schema(description = "해시태그")
    private final String tag;

    @Schema(description = "내용", example = "게시글 본문")
    private final String content;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    @Schema(description = "모집된 사용자들")
    private final List<RecruitedUsersDto> recruitedUsers;

    public ResponseSingleStudyDto(ResponseSingleWithDankookDto dto, Study study, int recruitedCount) {
        super(dto);
        this.title = study.getTitle();
        this.minStudentId = study.getMinStudentId();
        this.startTime = study.getStartTime();
        this.endTime = study.getEndTime();
        this.content = study.getContent();
        this.tag = study.getTag().getName();
        this.recruitedCount = recruitedCount;
        this.recruitedUsers = study.getUsers().stream()
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
    }
}
