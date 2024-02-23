package com.dku.council.domain.oauth.model.dto.request;

import lombok.Getter;

@Getter
public class OAuthTarget {
    private final String grantType;
    private final String code;
    private final String codeVerifier;

    private OAuthTarget(String grantType, String code, String codeVerifier) {
        this.grantType = grantType;
        this.code = code;
        this.codeVerifier = codeVerifier;
    }

    public static OAuthTarget of(String grantType, String code, String codeVerifier) {
        return new OAuthTarget(grantType, code, codeVerifier);
    }
}
