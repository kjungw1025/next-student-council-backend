package com.dku.council.domain.with_dankook.model.dto.request;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.type.Study;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class RequestCreateStudyDto extends RequestCreateWithDankookDto<Study> {
    @NotBlank
    @Schema(description = "제목", example = "제목")
    private final String title;

    @NotNull
    @Schema(description = "최소 학번", example = "19")
    private final int minStudentId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "스터디 시작 시간", example = "2023-12-25 17:30")
    private final LocalDateTime startTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "스터디 끝나는 시간", example = "2023-12-25 18:30")
    private final LocalDateTime endTime;

    @Schema(description = "해시태그", example = "자격증")
    private final String tag;

    @NotBlank
    @Schema(description = "본문", example = "내용")
    private final String content;

    @JsonCreator
    public RequestCreateStudyDto (@JsonProperty("title") @NotBlank String title,
                                  @JsonProperty("minStudentId") @NotBlank int minStudentId,
                                  @JsonProperty("startTime") @NotBlank LocalDateTime startTime,
                                  @JsonProperty("endTime") @NotBlank LocalDateTime endTime,
                                  @JsonProperty("tag") String tag,
                                  @JsonProperty("content") @NotBlank String content) {
        this.title = title;
        this.minStudentId = minStudentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tag = tag;
        this.content = content;
    }

    public Study toEntity(User user) {
        return Study.builder()
                .title(title)
                .minStudentId(minStudentId)
                .startTime(startTime)
                .endTime(endTime)
                .content(content)
                .user(user)
                .build();
    }
}
