package com.dku.council.domain.oauth.model.dto.response;

import lombok.Getter;

@Getter
public class OauthLoginResponse {
    private final String authCode;

    private OauthLoginResponse(String authCode) {
        this.authCode = authCode;
    }
    public static OauthLoginResponse from(String authCode) {
        return new OauthLoginResponse(authCode);
    }
}
