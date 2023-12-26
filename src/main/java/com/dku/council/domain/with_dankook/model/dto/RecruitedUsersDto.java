package com.dku.council.domain.with_dankook.model.dto;

import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RecruitedUsersDto {

    @Schema(description = "닉네임", example = "닉네임")
    private final String nickname;

    @Schema(description = "학과", example = "컴퓨터공학과")
    private final String majorName;

    @Schema(description = "학번", example = "19")
    private final String studentId;

    public RecruitedUsersDto(WithDankookUser user) {
        this.nickname = user.getParticipant().getNickname();
        this.majorName = user.getParticipant().getMajor().getName();
        this.studentId = user.getParticipant().getStudentId();
    }
}
