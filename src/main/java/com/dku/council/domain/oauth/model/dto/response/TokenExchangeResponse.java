package com.dku.council.domain.oauth.model.dto.response;

import lombok.Getter;

@Getter
public class TokenExchangeResponse {
    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer";
    private String scope;

    private TokenExchangeResponse(String accessToken, String refreshToken, String scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scope = scope;
    }

    public static TokenExchangeResponse of(String accessToken, String refreshToken, String scope) {
        return new TokenExchangeResponse(accessToken, refreshToken, scope);
    }
}
