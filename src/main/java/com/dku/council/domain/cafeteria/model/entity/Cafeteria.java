package com.dku.council.domain.cafeteria.model.entity;

import com.dku.council.global.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafeteria extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cafeteria_id")
    private Long id;

    @NotNull
    @Column(name = "meal_date")
    private LocalDate mealDate;

    @NotNull
    @Lob
    private String breakfast;

    @NotNull
    @Lob
    private String lunch;

    @NotNull
    @Lob
    private String dinner;

    @NotNull
    @Lob
    private String other;

    @Builder
    private Cafeteria(@NonNull LocalDate mealDate,
                      @NonNull String breakfast,
                      @NonNull String lunch,
                      @NonNull String dinner,
                      @NonNull String other) {
        this.mealDate = mealDate;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.other = other;
    }
}
