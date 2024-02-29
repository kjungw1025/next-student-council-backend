package com.dku.council.domain.oauth.model.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenExchangeRequest {
    @NotBlank(message = "grantType을 입력해주세요.")
    String grantType;
    @NotBlank(message = "clientId를 입력해주세요.")
    String clientId;
    @NotBlank(message = "redirectUri를 입력해주세요.")
    String redirectUri;
    @NotBlank(message = "clientSecret을 입력해주세요.")
    String clientSecret;
    @NotBlank(message = "code를 입력해주세요.")
    String code;
    @NotBlank(message = "codeVerifier를 입력해주세요.")
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
