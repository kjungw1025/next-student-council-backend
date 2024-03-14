package com.dku.council.mock;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.dto.response.ResponseLineUpDto;
import com.dku.council.domain.danfesta.model.entity.LineUp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LineUpMock {

    public static List<LineUp> createList(String prefix, int size, int year, int month, int dayOfMonth, FestivalDate festivalDate, boolean isOpened) {
        List<LineUp> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LineUp lineUp = LineUp.builder()
                    .singer(prefix + i)
                    .description(prefix + i + " description")
                    .performanceTime(LocalDate.of(year, month, dayOfMonth))
                    .festivalDate(festivalDate)
                    .isOpened(isOpened)
                    .build();
            result.add(lineUp);
        }
        return result;
    }

    public static ResponseLineUpDto createDummyDto(Long id, FestivalDate festivalDate, boolean isOpened) {
        return new ResponseLineUpDto(id, "singer", new ArrayList<>(), "description",
                LocalDate.of(2021, 1, 1), festivalDate, isOpened);
    }
}
