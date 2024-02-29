package com.dku.council.domain.with_dankook.model.dto.request;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.type.EatingAlone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class RequestCreateEatingAloneDto extends RequestCreateWithDankookDto<EatingAlone> {

    @NotBlank
    @Schema(description = "제목", example = "제목")
    private final String title;

    @NotBlank
    @Schema(description = "본문", example = "내용")
    private final String content;

    @Override
    public EatingAlone toEntity(User user) {
        return EatingAlone.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
