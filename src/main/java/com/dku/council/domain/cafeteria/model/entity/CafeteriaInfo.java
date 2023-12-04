package com.dku.council.domain.cafeteria.model.entity;

import com.dku.council.global.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeteriaInfo extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cafeteria_info_id")
    private Long id;

    @NotNull
    @Column(name = "meal_date")
    private LocalDate mealDate;

    @NotNull
    @Lob
    private String origin;

    @NotNull
    @Lob
    private String allergy;

    @Builder
    private CafeteriaInfo(@NonNull LocalDate mealDate,
                          @NonNull String origin,
                          @NonNull String allergy) {
        this.mealDate = mealDate;
        this.origin = origin;
        this.allergy = allergy;
    }
}
