package com.dku.council.domain.oauth.model.dto.request;

import com.dku.council.domain.oauth.exception.InvalidCodeChallengeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthCachePayload {
    Long userId;
    String codeChallenge;
    String codeChallengeMethod;
    String scope;

    public static OauthCachePayload of(Long userId, String codeChallenge, String codeChallengeMethod, String scope) {
        return new OauthCachePayload(userId, codeChallenge, codeChallengeMethod, scope);
    }

    public void checkCodeChallenge(String codeChallenge) {
        if (!this.codeChallenge.equals(codeChallenge)) {
            throw new InvalidCodeChallengeException(codeChallenge);
        }
    }
}
