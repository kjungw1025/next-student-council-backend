package com.dku.council.domain.oauth.model.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class OauthRequest {
    @NotBlank(message = "codeChallenge를 입력해주세요.")
    private final String codeChallenge;
    private String codeChallengeMethod = "S256";
    @NotBlank(message = "clientId를 입력해주세요.")
    private final String clientId;
    @NotBlank(message = "redirectUri를 입력해주세요.")
    private final String redirectUri;
    @NotBlank(message = "responseType을 입력해주세요.")
    private final String responseType;
    @NotBlank(message = "scope를 입력해주세요.")
    private final String scope;

    private OauthRequest(String codeChallenge, String codeChallengeMethod, String clientId,
                         String redirectUri, String responseType, String scope) {
        this.codeChallenge = codeChallenge;
        this.codeChallengeMethod = codeChallengeMethod;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.responseType = responseType;
        this.scope = scope;
    }

    private OauthRequest(String codeChallenge, String clientId, String redirectUri, String responseType, String scope) {
        this.codeChallenge = codeChallenge;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.responseType = responseType;
        this.scope = scope;
    }

    public static OauthRequest of(String codeChallenge, String codeChallengeMethod, String clientId,
                                  String redirectUri, String responseType, String scope) {
        if (codeChallengeMethod == null) {
            return OauthRequest.of(codeChallenge, clientId, redirectUri, responseType, scope);
        }
        return new OauthRequest(codeChallenge, codeChallengeMethod, clientId, redirectUri, responseType, scope);
    }

    public static OauthRequest of(String codeChallenge, String clientId,
                                  String redirectUri, String responseType, String scope) {
        return new OauthRequest(codeChallenge, clientId, redirectUri, responseType, scope);
    }
}
