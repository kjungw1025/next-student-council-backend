package com.dku.council.domain.danfesta.model.dto.response;

import com.dku.council.domain.danfesta.model.entity.LineUp;
import lombok.Getter;


@Getter
public class ResponseFestivalDateDto {

    private final String performanceTime;

    private final String festivalDate;

    public ResponseFestivalDateDto(LineUp lineup) {
        this.performanceTime = lineup.getPerformanceTime().toString();
        this.festivalDate = lineup.getFestivalDate().name();
    }
}
