package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.dto.TradeImageDto;
import com.dku.council.domain.with_dankook.model.entity.type.Trade;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class SummarizedTradeDto extends SummarizedWithDankookDto{

    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @Schema(description = "가격", example = "10000")
    private final int price;

    @Schema(description = "내용", example = "게시글 본문")
    private final String content;

    @Schema(description = "거래 장소", example = "단국대학교 정문")
    private final String tradePlace;

    @Schema(description = "이미지 목록")
    private final List<TradeImageDto> images;

    public SummarizedTradeDto(SummarizedWithDankookDto dto, Trade trade, ObjectUploadContext context){
        super(dto);
        this.title = trade.getTitle();
        this.price = trade.getPrice();
        this.content = trade.getContent();
        this.tradePlace = trade.getTradePlace();
        this.images = TradeImageDto.listOf(context, trade.getImages());
    }
}
