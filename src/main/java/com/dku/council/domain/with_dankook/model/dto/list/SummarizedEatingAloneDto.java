package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.entity.type.EatingAlone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SummarizedEatingAloneDto extends SummarizedWithDankookDto {

    @Schema(description = "제목", example = "저녁 같이 먹을 사람 구해요")
    private final String title;

    @Schema(description = "내용", example = "저녁 같이 먹을 사람 구해요")
    private final String content;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    public SummarizedEatingAloneDto(SummarizedWithDankookDto dto, EatingAlone eatingAlone, int recruitedCount) {
        super(dto);
        this.title = eatingAlone.getTitle();
        this.content = eatingAlone.getContent();
        this.recruitedCount = recruitedCount;
    }
}
