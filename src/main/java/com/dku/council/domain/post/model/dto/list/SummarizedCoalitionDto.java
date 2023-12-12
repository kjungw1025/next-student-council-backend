package com.dku.council.domain.post.model.dto.list;

import com.dku.council.domain.post.model.entity.posttype.Coalition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SummarizedCoalitionDto extends SummarizedGenericPostDto{

    @Schema
    private final String coalitionType;

    public SummarizedCoalitionDto(SummarizedGenericPostDto dto, Coalition coalition) {
        super(dto);
        this.coalitionType = coalition.getCoalitionType().toString();
    }

}
