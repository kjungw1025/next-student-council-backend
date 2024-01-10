package com.dku.council.domain.with_dankook.model;

public enum ParticipantStatus {
    /**
     * 참여자가 유효한 상태
     */
    VALID,

    /**
     * 참여자가 신청 대기중인 상태
     */
    WAITING,

    /**
     * 참여자가 유효하지 않은 상태
     */
    INVALID
}
