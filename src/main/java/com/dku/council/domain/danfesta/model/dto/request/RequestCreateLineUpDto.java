package com.dku.council.domain.danfesta.model.dto.request;

import com.dku.council.domain.danfesta.model.FestivalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class RequestCreateLineUpDto {

    @Schema(description = "가수", example = "아이유")
    private final String singer;

    @Schema(description = "가수 이미지 목록")
    private final List<MultipartFile> images;

    @Schema(description = "설명", example = "아이유의 공연입니다.")
    private final String description;

    @Schema(description = "공연 날짜", example = "2024-05-20")
    private final String performanceDate;

    @Schema(description = "축제 일자", example = "FIRST_DAY")
    private final FestivalDate festivalDate;

    public RequestCreateLineUpDto(String singer, List<MultipartFile> images, String description, String performanceDate, FestivalDate festivalDate) {
        this.singer = singer;
        this.images = Objects.requireNonNullElseGet(images, ArrayList::new);
        this.description = description;
        this.performanceDate = performanceDate;
        this.festivalDate = festivalDate;
    }
}
