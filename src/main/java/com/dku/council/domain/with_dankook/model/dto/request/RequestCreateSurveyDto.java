package com.dku.council.domain.with_dankook.model.dto.request;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.CleanUpCount;
import com.dku.council.domain.with_dankook.model.SleepTime;
import com.dku.council.domain.with_dankook.model.entity.RoomMateSurvey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RequestCreateSurveyDto {

    @NotNull
    @Schema(description = "MBTI", example = "INTJ")
    private final String mbti;

    @NotNull
    @Schema(description = "수면습관", example = "true")
    private final boolean sleepHabit;

    @NotNull
    @Schema(description = "수면민감도", example = "true")
    private final boolean sleepSensitive;

    @NotNull
    @Schema(description = "흡연", example = "true")
    private final boolean smoking;

    @NotNull
    @Schema(description = "소음습관", example = "true")
    private final boolean noiseHabit;

    @Schema(description = "소음습관 상세", example = "")
    private final String noiseHabitDetail;

    @NotNull
    @Schema(description = "수면시간", example = "AVG_TWELVE")
    private final SleepTime sleepTime;

    @NotNull
    @Schema(description = "청소횟수", example = "ONCE_UNDER_WEEK")
    private final CleanUpCount cleanUpCount;

    @Schema(description = "기타 성향", example = "")
    private final String tendency;

    public RequestCreateSurveyDto(@NotBlank String mbti,
                                  @NotNull boolean sleepHabit,
                                  @NotNull boolean sleepSensitive,
                                  @NotNull boolean smoking,
                                  @NotNull boolean noiseHabit,
                                  String noiseHabitDetail,
                                  @NotNull SleepTime sleepTime,
                                  @NotNull CleanUpCount cleanUpCount,
                                  String tendency) {
        this.mbti = mbti.toUpperCase();
        this.sleepHabit = sleepHabit;
        this.sleepSensitive = sleepSensitive;
        this.smoking = smoking;
        this.noiseHabit = noiseHabit;
        this.noiseHabitDetail = noiseHabit ? noiseHabitDetail : null;
        this.sleepTime = sleepTime;
        this.cleanUpCount = cleanUpCount;
        this.tendency = tendency;
    }

    public RoomMateSurvey toEntity(User user) {
        return RoomMateSurvey.builder()
                .user(user)
                .mbti(mbti)
                .sleepHabit(sleepHabit)
                .sleepSensitive(sleepSensitive)
                .smoking(smoking)
                .noiseHabit(noiseHabit)
                .noiseHabitDetail(noiseHabitDetail)
                .sleepTime(sleepTime)
                .cleanUpCount(cleanUpCount)
                .tendency(tendency)
                .build();
    }
}
