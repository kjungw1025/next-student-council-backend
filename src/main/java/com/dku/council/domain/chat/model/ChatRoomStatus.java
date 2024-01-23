package com.dku.council.domain.chat.model;

public enum ChatRoomStatus {
    /**
     * 활성화 상태
     */
    ACTIVE,

    /**
     * 닫힌 상태
     */
    CLOSED,

    /**
     * 삭제된 상태
     */
    DELETED,

    /**
     * 운영자에 의해 삭제된 상태
     */
    DELETED_BY_ADMIN
}
