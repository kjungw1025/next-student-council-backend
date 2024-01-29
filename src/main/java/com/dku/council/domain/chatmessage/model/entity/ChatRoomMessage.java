package com.dku.council.domain.chatmessage.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.dku.council.domain.chatmessage.model.ChatRoomMessageId;
import com.dku.council.global.config.DynamoDBConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@DynamoDBTable(tableName = "ChatRoomMessage")
@Getter
@Setter
@NoArgsConstructor()
public class ChatRoomMessage {

    @Id
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private ChatRoomMessageId chatRoomMessageId;

    @DynamoDBHashKey(attributeName = "roomId")
    public String getRoomId() {
        return chatRoomMessageId != null ? chatRoomMessageId.getRoomId() : null;
    }

    public void setRoomId(String roomId) {
        if (chatRoomMessageId == null) {
            chatRoomMessageId = new ChatRoomMessageId();
        }
        chatRoomMessageId.setRoomId(roomId);
    }

    @DynamoDBRangeKey(attributeName = "createdAt")
    @DynamoDBTypeConverted(converter = DynamoDBConfig.LocalDateTimeConverter.class)
    public LocalDateTime getCreatedAt() {
        return chatRoomMessageId != null ? chatRoomMessageId.getCreatedAt() : null;
    }

    @DynamoDBTypeConverted(converter = DynamoDBConfig.LocalDateTimeConverter.class)
    public void setCreatedAt(LocalDateTime createdAt) {
        if (chatRoomMessageId == null) {
            chatRoomMessageId = new ChatRoomMessageId();
        }
        chatRoomMessageId.setCreatedAt(createdAt);
    }

    @DynamoDBAttribute
    private String messageType;

    @DynamoDBAttribute
    private Long userId;

    @DynamoDBAttribute
    private String userNickname;

    @DynamoDBAttribute
    private String content;
}