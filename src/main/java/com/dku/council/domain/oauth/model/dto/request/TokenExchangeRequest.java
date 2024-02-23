package com.dku.council.domain.oauth.model.dto.request;

import lombok.Getter;

@Getter
public class TokenExchangeRequest {
    String grantType;
    String clientId;
    String redirectUri;
    String clientSecret;
    String code;
    String codeVerifier;

    private TokenExchangeRequest(String grantType, String clientId, String redirectUri,
                                 String clientSecret, String code, String codeVerifier) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.code = code;
        this.codeVerifier = codeVerifier;
    }

    public static TokenExchangeRequest of(String grantType, String clientId, String redirectUri,
                                          String clientSecret, String code, String codeVerifier) {
        return new TokenExchangeRequest(grantType, clientId, redirectUri, clientSecret, code, codeVerifier);
    }

    public ClientInfo toClientInfo() {
        return ClientInfo.of(clientId, clientSecret, redirectUri);
    }

    public OAuthTarget toAuthTarget() {
        return OAuthTarget.of(grantType, code, codeVerifier);
    }
}
