package com.dku.council.domain.oauth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthCachePayload {
    String authCode;
    String codeChallenge;
    String codeChallengeMethod;
    String scope;

    public static OauthCachePayload of(String authCode, String codeChallenge, String codeChallengeMethod, String scope) {
        return new OauthCachePayload(authCode, codeChallenge, codeChallengeMethod, scope);
    }
}
