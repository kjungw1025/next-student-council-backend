package com.dku.council.domain.cafeteria.controller;

import com.dku.council.domain.cafeteria.model.dto.response.ResponseCafeteriaDto;
import com.dku.council.domain.cafeteria.model.dto.response.ResponseCafeteriaInfoDto;
import com.dku.council.domain.cafeteria.service.CafeteriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "학식 정보", description = "오늘의 학식 정보 관련 api")
@RestController
@RequestMapping("/cafeteria")
@RequiredArgsConstructor
public class CafeteriaController {

    private final CafeteriaService cafeteriaService;

    @GetMapping("meal/today")
    @ResponseBody
    public ResponseCafeteriaDto today() {
        LocalDate today = LocalDate.now();
        return cafeteriaService.findByMealDate(today).get(0);
    }

    @GetMapping("meal/labeling")
    @ResponseBody
    public ResponseCafeteriaInfoDto foodLabeling() {
        LocalDate today = LocalDate.now();
        return cafeteriaService.findMealInfo(today).get(0);
    }
}
