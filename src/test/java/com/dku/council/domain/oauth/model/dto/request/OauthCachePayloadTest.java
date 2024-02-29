package com.dku.council.domain.oauth.model.dto.request;

import com.dku.council.domain.oauth.exception.InvalidCodeChallengeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OauthCachePayloadTest {
    @Test
    void createOauthCachePayload() {
        // given
        Long userId = 1L;
        String codeChallenge = "codeChallenge";
        String codeChallengeMethod = "codeChallengeMethod";
        String scope = "scope";

        // when
        OauthCachePayload payload = OauthCachePayload.of(userId, codeChallenge, codeChallengeMethod, scope);

        // then
        assertEquals(userId, payload.getUserId());
        assertEquals(codeChallenge, payload.getCodeChallenge());
        assertEquals(codeChallengeMethod, payload.getCodeChallengeMethod());
        assertEquals(scope, payload.getScope());
    }

    @Test
    void checkWhenCodeChallengeMatches() {
        // given
        String codeChallenge = "codeChallenge";
        OauthCachePayload payload = OauthCachePayload.of(1L, codeChallenge, "codeChallengeMethod", "scope");

        // when, then
        assertDoesNotThrow(() -> payload.checkCodeChallenge(codeChallenge));
    }

    @Test
    void throwExceptionWhenCodeChallengeDoesNotMatch() {
        // given
        String codeChallenge = "codeChallenge";
        OauthCachePayload payload = OauthCachePayload.of(1L, codeChallenge, "codeChallengeMethod", "scope");

        // when, then
        assertThrows(InvalidCodeChallengeException.class, () -> payload.checkCodeChallenge("differentCodeChallenge"));
    }
}