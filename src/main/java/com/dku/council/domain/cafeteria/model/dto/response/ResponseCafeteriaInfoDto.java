package com.dku.council.domain.cafeteria.model.dto.response;

import com.dku.council.domain.cafeteria.model.entity.CafeteriaInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class ResponseCafeteriaInfoDto {
    private final LocalDate mealDate;

    private final String origin;

    private final String allergy;

    @Builder
    private ResponseCafeteriaInfoDto(CafeteriaInfo cafeteriaInfo) {
        this.mealDate = cafeteriaInfo.getMealDate();
        this.origin = cafeteriaInfo.getOrigin();
        this.allergy = cafeteriaInfo.getAllergy();
    }
}
