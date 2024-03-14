package com.dku.council.domain.danfesta.controller;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.entity.LineUp;
import com.dku.council.domain.danfesta.repository.LineUpRepository;
import com.dku.council.domain.user.repository.MajorRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.mock.LineUpMock;
import com.dku.council.util.base.AbstractAuthControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LineUpControllerTest extends AbstractAuthControllerTest{

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LineUpRepository lineUpRepository;

    private List<LineUp> notOpenedList;

    private List<LineUp> openedList;

    @BeforeEach
    void setup() {
        lineUpRepository.deleteAll();

        notOpenedList = LineUpMock.createList("singer", 3, 2024, 5, 20, FestivalDate.FIRST_DAY, false);
        openedList = LineUpMock.createList("singer", 3, 2024, 5, 21, FestivalDate.SECOND_DAY, true);
        lineUpRepository.saveAll(notOpenedList);
        lineUpRepository.saveAll(openedList);
    }

    @Test
    @DisplayName("라인업 목록 조회 - 공개되지 않았을 때")
    public void listLineUpWithNotOpened() throws Exception {

        // when
        ResultActions result = mvc.perform(get("/line-up")
                .param("festivalDate", FestivalDate.FIRST_DAY.name()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(notOpenedList.size())))
                .andExpect(jsonPath("$[0].singer", is("공개 예정")))
                .andExpect(jsonPath("$[0].description", is("공개 예정")))
                .andExpect(jsonPath("$[0].opened", is(false)));
    }

    @Test
    @DisplayName("라인업 목록 조회 - 공개되었을 때")
    public void listLineUpWithOpened() throws Exception {
        // when
        ResultActions result = mvc.perform(get("/line-up")
                .param("festivalDate", FestivalDate.SECOND_DAY.name()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(openedList.size())))
                .andExpect(jsonPath("$[0].singer", is(openedList.get(0).getSinger())))
                .andExpect(jsonPath("$[0].description", is(openedList.get(0).getDescription())))
                .andExpect(jsonPath("$[0].opened", is(true)));

    }
}