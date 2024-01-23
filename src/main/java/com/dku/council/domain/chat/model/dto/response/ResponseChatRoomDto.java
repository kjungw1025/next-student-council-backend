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
    private final String roomPwd; // 채팅방 삭제시 필요한 pwd
    private final boolean secretCheck; // 채팅방 잠금 여부
}
