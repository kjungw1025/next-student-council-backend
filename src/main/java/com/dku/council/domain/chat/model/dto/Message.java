package com.dku.council.domain.chat.model.dto;

import com.dku.council.domain.chat.model.FileType;
import com.dku.council.domain.chat.model.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @NotNull
    private LocalDateTime messageTime;

    private String fileName;

    private String fileUrl;

    private FileType fileType;

    @Builder
    private Message(MessageType type,
                    String roomId,
                    String sender,
                    String message,
                    LocalDateTime messageTime,
                    String fileName,
                    String fileUrl,
                    FileType fileType) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.messageTime = messageTime;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
    }
}
