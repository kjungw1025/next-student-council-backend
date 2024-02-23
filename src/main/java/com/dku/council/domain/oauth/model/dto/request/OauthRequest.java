package com.dku.council.domain.oauth.model.dto.request;

import lombok.Getter;

@Getter
public class OauthRequest {
    private final String codeChallenge;
    private String codeChallengeMethod = "S256";
    private final String clientId;
    private final String redirectUri;
    private final String responseType;
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
