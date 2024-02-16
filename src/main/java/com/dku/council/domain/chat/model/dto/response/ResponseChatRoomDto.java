package com.dku.council.domain.chat.model.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseChatRoomDto {
    private final String roomId;
    private final String roomName;
    private final int userCount;
    private final int maxUserCount;
}
