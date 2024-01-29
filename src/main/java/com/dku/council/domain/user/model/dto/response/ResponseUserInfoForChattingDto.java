package com.dku.council.domain.user.model.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseUserInfoForChattingDto {
    private final Long userId;
    private final String nickname;
    private final boolean isAdmin;
}
