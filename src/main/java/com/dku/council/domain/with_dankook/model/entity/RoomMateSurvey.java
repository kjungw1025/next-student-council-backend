package com.dku.council.domain.with_dankook.model.entity;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.CleanUpCount;
import com.dku.council.domain.with_dankook.model.SleepTime;
import com.dku.council.global.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMateSurvey extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "room_mate_survey_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String mbti;

    @NotNull
    private boolean sleepHabit;

    @NotNull
    private boolean sleepSensitive;

    @NotNull
    private boolean smoking;

    @NotNull
    private boolean noiseHabit;

    private String noiseHabitDetail;

    @Enumerated(EnumType.STRING)
    private SleepTime sleepTime;

    @Enumerated(EnumType.STRING)
    private CleanUpCount cleanUpCount;

    @Lob
    private String tendency;

    @Builder
    public RoomMateSurvey(User user, String mbti, boolean sleepHabit, boolean sleepSensitive, boolean smoking, boolean noiseHabit, String noiseHabitDetail, SleepTime sleepTime, CleanUpCount cleanUpCount, String tendency) {
        this.user = user;
        this.mbti = mbti;
        this.sleepHabit = sleepHabit;
        this.sleepSensitive = sleepSensitive;
        this.smoking = smoking;
        this.noiseHabit = noiseHabit;
        this.noiseHabitDetail = noiseHabitDetail;
        this.sleepTime = sleepTime;
        this.cleanUpCount = cleanUpCount;
        this.tendency = tendency;
    }
}
