package com.dku.council.domain.cafeteria.service;

import com.dku.council.domain.cafeteria.model.dto.response.ResponseCafeteriaDto;
import com.dku.council.domain.cafeteria.model.dto.response.ResponseCafeteriaInfoDto;
import com.dku.council.domain.cafeteria.model.entity.Cafeteria;
import com.dku.council.domain.cafeteria.model.entity.CafeteriaInfo;
import com.dku.council.domain.cafeteria.repository.CafeteriaInfoRepository;
import com.dku.council.domain.cafeteria.repository.CafeteriaRepository;
import com.dku.council.infra.dku.exception.DkuFailedCrawlingException;
import com.dku.council.infra.dku.service.DkuCafeteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class CafeteriaService {
    private final DkuCafeteriaService dkuCafeteriaService;
    private final CafeteriaRepository cafeteriaRepository;
    private final CafeteriaInfoRepository cafeteriaInfoRepository;

    private final String nonExistStr = "해당 날짜에 학식 정보가 없습니다.";

    public void loadCafeteria() {
        dkuCafeteriaService.crawlCafeteria();
    }

    public List<ResponseCafeteriaDto> findByMealDate(LocalDate date) {
        if (cafeteriaRepository.findByMealDate(date).isPresent()) {
            Cafeteria cafeteria = cafeteriaRepository.findByMealDate(date).orElseThrow(DkuFailedCrawlingException::new);
            ResponseCafeteriaDto dto = ResponseCafeteriaDto.builder()
                    .cafeteria(cafeteria)
                    .build();
            List<ResponseCafeteriaDto> result = new ArrayList<>();
            result.add(dto);
            return result;
        }
        else {
            return nonExistMealDate(date);
        }
    }

    public List<ResponseCafeteriaInfoDto> findMealInfo(LocalDate date) {
        if (cafeteriaInfoRepository.findByMealDate(date).isPresent()) {
            CafeteriaInfo cafeteriaInfo = cafeteriaInfoRepository.findByMealDate(date).orElseThrow(DkuFailedCrawlingException::new);
            ResponseCafeteriaInfoDto dto = ResponseCafeteriaInfoDto.builder()
                    .cafeteriaInfo(cafeteriaInfo)
                    .build();
            List<ResponseCafeteriaInfoDto> result = new ArrayList<>();
            result.add(dto);
            return result;
        }
        else {
            return nonExistInfoDate(date);
        }
    }

    private List<ResponseCafeteriaDto> nonExistMealDate(LocalDate date) {
        List<ResponseCafeteriaDto> result = new ArrayList<>();
        result.add(new ResponseCafeteriaDto(date, nonExistStr, nonExistStr, nonExistStr, nonExistStr));
        return result;
    }

    private List<ResponseCafeteriaInfoDto> nonExistInfoDate(LocalDate date) {
        List<ResponseCafeteriaInfoDto> result = new ArrayList<>();
        result.add(new ResponseCafeteriaInfoDto(date, nonExistStr, nonExistStr));
        return result;
    }
}
