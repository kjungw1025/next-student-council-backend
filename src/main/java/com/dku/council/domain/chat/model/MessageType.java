package com.dku.council.domain.chat.model;

public enum MessageType {
    /**
     * 채팅방 입장
     */
    ENTER,

    /**
     * 채팅방 대화중
     * (해당 채팅방을 sub하고 있는 모든 client들에게 전달됨)
     */
    TALK,

    /**
     * 채팅방 퇴장
     */
    LEAVE;
}