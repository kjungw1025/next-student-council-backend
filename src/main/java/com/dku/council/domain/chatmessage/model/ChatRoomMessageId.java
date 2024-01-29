package com.dku.council.domain.chatmessage.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.dku.council.global.config.DynamoDBConfig;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomMessageId implements Serializable {
    private static final long serialVersionUID = 1L;

    @DynamoDBHashKey
    private String roomId;

    @DynamoDBRangeKey
    @DynamoDBTypeConverted(converter = DynamoDBConfig.LocalDateTimeConverter.class)
    private LocalDateTime createdAt;
}
