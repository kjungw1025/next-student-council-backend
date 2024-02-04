package com.dku.council.domain.chat.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class RequestChatFileDto {

    @NotBlank
    @Schema(description = "채팅방 번호", example = "d118101z-c737-4253-9911-ea2579405f42")
    private final String roomId;

    @Schema(description = "첨부 파일 목록")
    private final List<MultipartFile> files;

    public RequestChatFileDto(String roomId, List<MultipartFile> files) {
        this.roomId = roomId;
        this.files = Objects.requireNonNullElseGet(files, ArrayList::new);
    }
}
