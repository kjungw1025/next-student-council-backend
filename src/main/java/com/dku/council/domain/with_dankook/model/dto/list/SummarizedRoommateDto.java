package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.entity.type.Roommate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Getter
public class SummarizedRoommateDto extends SummarizedWithDankookDto {

    @Schema(description = "작성자 닉네임", example = "익명")
    private final String nickname;

    @Schema(description = "작성자 학번", example = "12345678")
    private final String studentId;

    @Schema(description = "작성자 전공", example = "SW융합대학")
    private final String major;

    @Schema(description = "작성자 학과", example = "소프트웨어학과")
    private final String department;

    @Schema(description = "기숙사 생활관", example = "웅비홀")
    private final String livingPlace;

    @Schema(description = "거주 기간", example = "SEMESTER")
    private final String residenceDuration;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    @Schema(description = "설문조사 일치 수", example = "1")
    private final int equalCount;

    @Schema(description = "내가 작성한 게시글인지 여부", example = "true")
    private final boolean isMine;

    public SummarizedRoommateDto(SummarizedWithDankookDto dto, Roommate roommate, MessageSource messageSource, int recruitedCount, int equalCount, boolean isMine) {
        super(dto);
        this.nickname = roommate.getMasterUser().getNickname();
        this.studentId = roommate.getMasterUser().getStudentId();
        this.major = roommate.getMasterUser().getMajor().getName();
        this.department = roommate.getMasterUser().getMajor().getDepartment();
        this.livingPlace = roommate.getLivingPlace();
        this.residenceDuration = messageSource.getMessage("withdankook.residence.duration." + roommate.getResidenceDuration().name().toLowerCase(), null , LocaleContextHolder.getLocale());
        this.recruitedCount = recruitedCount;
        this.equalCount = equalCount;
        this.isMine = isMine;
    }
}
