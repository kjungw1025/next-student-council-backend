package com.dku.council.domain.danfesta.model;

import com.dku.council.domain.danfesta.exception.NotFoundFestivalDateException;
import lombok.Getter;

@Getter
public enum FestivalDate {
    FIRST_DAY("1일차"),
    SECOND_DAY("2일차"),
    THIRD_DAY("3일차");

    private final String name;

    FestivalDate(String name) {
        this.name = name;
    }

    public static FestivalDate of(String name) {
        for (FestivalDate festivalDate : values()) {
            if (festivalDate.getName().equals(name)) {
                return festivalDate;
            }
        }
        throw new NotFoundFestivalDateException();
    }
}
