package com.dku.council.domain.cafeteria.model.dto.response;

import com.dku.council.domain.cafeteria.model.entity.Cafeteria;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class ResponseCafeteriaDto {
    private final LocalDate mealDate;

    private final String breakfast;

    private final String lunch;

    private final String dinner;

    private final String other;

    @Builder
    private ResponseCafeteriaDto(Cafeteria cafeteria) {
        this.mealDate = cafeteria.getMealDate();
        this.breakfast = cafeteria.getBreakfast();
        this.lunch = cafeteria.getLunch();
        this.dinner = cafeteria.getDinner();
        this.other = cafeteria.getOther();
    }
}
