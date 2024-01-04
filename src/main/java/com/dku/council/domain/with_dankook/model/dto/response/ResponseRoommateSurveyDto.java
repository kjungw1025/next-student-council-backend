package com.dku.council.domain.with_dankook.model.dto.response;

import com.dku.council.domain.with_dankook.model.entity.RoomMateSurvey;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseRoommateSurveyDto {

    private final String mbti;

    private final boolean sleephabit;

    private final boolean sleepSensitive;

    private final boolean smoking;

    private final boolean noiseHabit;

    private final String noiseHabitDetail;

    private final String sleepTime;

    private final String cleanUpCount;

    private final String tendency;

    @Builder
    public ResponseRoommateSurveyDto(RoomMateSurvey survey) {
        this.mbti = survey.getMbti();
        this.sleephabit = survey.isSleepHabit();
        this.sleepSensitive = survey.isSleepSensitive();
        this.smoking = survey.isSmoking();
        this.noiseHabit = survey.isNoiseHabit();
        this.noiseHabitDetail = survey.getNoiseHabitDetail();
        this.sleepTime = survey.getSleepTime().name();
        this.cleanUpCount = survey.getCleanUpCount().name();
        this.tendency = survey.getTendency();
    }

}
