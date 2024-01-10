package com.dku.council.domain.with_dankook.model.dto.request;


import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.type.Trade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class RequestCreateTradeDto extends RequestCreateWithDankookDto<Trade> {

    @NotBlank
    @Schema(description = "제목", example = "제목")
    private final String title;

    @NotNull
    @Schema(description = "가격", example = "10000")
    private final int price;

    @NotBlank
    @Schema(description = "본문", example = "내용")
    private final String content;

    @NotBlank
    @Schema(description = "거래 장소", example = "단국대학교 정문")
    private final String tradePlace;

    @Schema(description = "이미지 파일 목록")
    private final List<MultipartFile> images;

    public RequestCreateTradeDto(@NotBlank String title, @NotBlank int price, @NotBlank String content, @NotBlank String tradePlace, List<MultipartFile> images) {
        this.title = title;
        this.price = price;
        this.content = content;
        this.tradePlace = tradePlace;
        this.images = Objects.requireNonNullElseGet(images, ArrayList::new);
    }

    public Trade toEntity(User user) {
        return Trade.builder()
                .title(title)
                .price(price)
                .content(content)
                .tradePlace(tradePlace)
                .user(user)
                .build();
    }
}
