package com.dku.council.domain.with_dankook.model.dto.response;

import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseSingleWithDankookDto {

    @Schema(description = "게시글 아이디", example = "1")
    private final Long id;

    @Schema(description = "작성자", example = "익명")
    private final String author;

    @Schema(description = "생성 날짜")
    private final LocalDateTime createdAt;

    @Schema(description = "채팅 링크", example = "https://open.kakao.com/o/ghjgjgjg")
    private final String chatLink;

    @Schema(description = "좋아요 수", example = "26")
    private final int likes;

    @Schema(description = "내가 쓴 게시물인지?", example = "true")
    private final boolean isMine;

    @Schema(description = "내가 좋아요를 눌렀는지?", example = "false")
    private final boolean isLiked;

    @Schema(description = "닫힘 여부", example = "false")
    private final boolean isClosed;

    public ResponseSingleWithDankookDto(int likes, boolean isMine, boolean isLiked, WithDankook withDankook) {
        this.id = withDankook.getId();
        this.author = withDankook.getDisplayingUsername();
        this.createdAt = withDankook.getCreatedAt();
        this.chatLink = withDankook.getChatLink();
        this.likes = likes;
        this.isMine = isMine;
        this.isLiked = isLiked;
        this.isClosed = withDankook.isClosed();
    }

    public ResponseSingleWithDankookDto(ResponseSingleWithDankookDto copy) {
        this.id = copy.id;
        this.author = copy.author;
        this.createdAt = copy.createdAt;
        this.chatLink = copy.chatLink;
        this.likes = copy.likes;
        this.isMine = copy.isMine;
        this.isLiked = copy.isLiked;
        this.isClosed = copy.isClosed;
    }
}
