package com.dku.council.domain.chat.model.dto;

import com.dku.council.domain.chat.model.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@ToString
public class Message {

    @NotNull
    private MessageType type;

    private String roomId;

    @NotNull
    private String sender;

    @NotNull
    private String message;

    @Builder
    private Message(MessageType type,
                    String roomId,
                    String sender,
                    String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}