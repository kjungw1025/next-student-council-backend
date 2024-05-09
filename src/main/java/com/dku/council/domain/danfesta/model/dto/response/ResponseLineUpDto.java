package com.dku.council.domain.danfesta.model.dto.response;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.dto.LineUpImageDto;
import com.dku.council.domain.danfesta.model.entity.LineUp;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ResponseLineUpDto {

    private final Long id;

    private final String singer;

    private final List<LineUpImageDto> images;

    private final String description;

    private final LocalDateTime performanceTime;

    private final FestivalDate festivalDate;

    private final boolean isOpened;

    public ResponseLineUpDto(ObjectUploadContext context, LineUp lineUp) {
        this.id = lineUp.getId();
        this.singer = lineUp.getSinger();
        this.images = LineUpImageDto.listOf(context, lineUp.getImages());
        this.description = lineUp.getDescription();
        this.performanceTime = lineUp.getPerformanceTime();
        this.festivalDate = lineUp.getFestivalDate();
        this.isOpened = lineUp.isOpened();
    }
}
