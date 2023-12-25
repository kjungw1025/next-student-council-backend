package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SummarizedWithDankookDto {

    @Schema(description = "With-Dankook 게시글 아이디", example = "1")
    private final Long id;

    @Schema(description = "작성자", example = "익명")
    private final String author;

    @Schema(description = "생성 날짜")
    private final LocalDateTime createdAt;

    @Schema(description = "채팅 링크", example = "https://open.kakao.com/o/ghjgjgjg")
    private final String chatLink;

    @Schema(description = "게시글 타입", example = "TRADE")
    private final String type;

    public SummarizedWithDankookDto(int bodySize, WithDankook withDankook) {
        this.id = withDankook.getId();
        this.author = withDankook.getDisplayingUsername();
        this.createdAt = withDankook.getCreatedAt();
        this.chatLink = withDankook.getChatLink();
        this.type = withDankook.getClass().getSimpleName().toUpperCase();
    }

    public SummarizedWithDankookDto(SummarizedWithDankookDto copy) {
        this.id = copy.getId();
        this.author = copy.getAuthor();
        this.createdAt = copy.getCreatedAt();
        this.chatLink = copy.getChatLink();
        this.type = copy.getType();
    }
}
