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

    @Schema(description = "성별", example = "남자")
    private final String gender;

    @Schema(description = "학번", example = "201511111")
    private final String studentId;

    @Schema(description = "소속 대학", example = "SW융합대학")
    private final String department;

    @Schema(description = "소속 학과", example = "소프트웨어학과")
    private final String major;

    @Schema(description = "생성 날짜")
    private final LocalDateTime createdAt;

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
        this.gender = withDankook.getMasterUser().getGender();
        this.studentId = withDankook.getMasterUser().getStudentId();
        this.major = withDankook.getMasterUser().getMajor().getName();
        this.department = withDankook.getMasterUser().getMajor().getDepartment();
        this.createdAt = withDankook.getCreatedAt();
        this.likes = likes;
        this.isMine = isMine;
        this.isLiked = isLiked;
        this.isClosed = withDankook.isClosed();
    }

    public ResponseSingleWithDankookDto(ResponseSingleWithDankookDto copy) {
        this.id = copy.id;
        this.author = copy.author;
        this.gender = copy.gender;
        this.studentId = copy.studentId;
        this.major = copy.major;
        this.department = copy.department;
        this.createdAt = copy.createdAt;
        this.likes = copy.likes;
        this.isMine = copy.isMine;
        this.isLiked = copy.isLiked;
        this.isClosed = copy.isClosed;
    }
}
