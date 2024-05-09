package com.dku.council.domain.chat.model.dto.response;

import com.dku.council.domain.chat.model.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class ResponseChatDto {

    private final MessageType type;
    private final String roomId;
    private final String sender;
    private String message;
//    private final String time;

//    // 파일 업로드 관련 변수
//    private final String s3DataUrl; // 파일 업로드 url
//    private final String fileName; // 파일 이름
//    private final String fileDir; // s3 파일 경로

    @Builder
    private ResponseChatDto(@NotNull MessageType type,
                            @NotNull String roomId,
                            @NotNull String sender,
                            @NotNull String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }

    public void changeMessage(String message) {
        this.message = message;
    }
}
