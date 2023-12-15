package com.dku.council.domain.with_dankook.model;

public enum WithDankookStatus {
    /**
     * 활성화 상태
     */
    ACTIVE,

    /**
     * 닫힌 상태
     */
    CLOSED,

    /**
     * 모집이 마감된 상태
     */
    FULL,

    /**
     * 삭제된 상태
     */
    DELETED,

    /**
     * 운영자에 의해 삭제된 상태
     */
    DELETED_BY_ADMIN
}
