package com.dku.council.domain.with_dankook.model.dto.response;

import com.dku.council.domain.with_dankook.model.ParticipantStatus;
import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.RoomMateSurvey;
import com.dku.council.domain.with_dankook.model.entity.type.Roommate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResponseSingleRoommateDto extends ResponseSingleWithDankookDto {

    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @Schema(description = "최소 학번", example = "19")
    private final int minStudentId;

    @Schema(description = "거주지", example = "웅비홀")
    private final String livingPlace;

    @Schema(description = "거주 기간", example = "SEMESTER")
    private final String residenceDuration;

    @Schema(description = "룸메이트 설문조사 내용")
    private final ResponseRoommateSurveyDto survey;

    @Schema(description = "신청자 리스트")
    private final List<RecruitedUsersDto> applicantList;

    @Schema(description = "승인한 사용자 리스트")
    private final List<RecruitedUsersDto> approvedList;

    public ResponseSingleRoommateDto(ResponseSingleWithDankookDto dto, Roommate roommate, RoomMateSurvey survey, MessageSource messageSource) {
        super(dto);
        this.title = roommate.getTitle();
        this.minStudentId = roommate.getMinStudentId();
        this.livingPlace = roommate.getLivingPlace();
        this.residenceDuration = messageSource.getMessage("withdankook.residence.duration." + roommate.getResidenceDuration().name().toLowerCase(), null, LocaleContextHolder.getLocale());
        this.survey = new ResponseRoommateSurveyDto(survey);
        this.applicantList = roommate.getUsers().stream().filter((user) ->
                        user.getParticipantStatus() == ParticipantStatus.WAITING)
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
        this.approvedList = roommate.getUsers().stream().filter((user) ->
                        user.getParticipantStatus() == ParticipantStatus.VALID && !user.getParticipant().getId().equals(roommate.getMasterUser().getId()))
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
    }

}
