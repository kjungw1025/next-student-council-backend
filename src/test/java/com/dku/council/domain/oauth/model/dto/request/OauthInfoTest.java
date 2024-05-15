package com.dku.council.domain.oauth.model.dto.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OauthInfoTest {
    private String clientId;
    private String redirectUri;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String scope;
    private String responseType;

    @BeforeEach
    void setUp() {
        clientId = "clientId";
        redirectUri = "redirectUri";
        codeChallenge = "codeChallenge";
        codeChallengeMethod = "codeChallengeMethod";
        scope = "scope";
        responseType = "responseType";
    }

    @Test
    void createOauthInfoWithGivenParameters() {
        // when
        OauthInfo oauthInfo = OauthInfo.of(clientId, redirectUri, codeChallenge, codeChallengeMethod, scope, responseType);

        // then
        assertEquals(clientId, oauthInfo.getClientId());
        assertEquals(redirectUri, oauthInfo.getRedirectUri());
        assertEquals(codeChallenge, oauthInfo.getCodeChallenge());
        assertEquals(codeChallengeMethod, oauthInfo.getCodeChallengeMethod());
        assertEquals(scope, oauthInfo.getScope());
        assertEquals(responseType, oauthInfo.getResponseType());
    }

    @Test
    void convertToCachePayloadWithGivenUserId() {
        // given
        Long userId = 1L;
        OauthInfo oauthInfo = OauthInfo.of(clientId, redirectUri, codeChallenge, codeChallengeMethod, scope, responseType);

        // when
        OauthCachePayload payload = oauthInfo.toCachePayload(userId);

        // then
        assertEquals(userId, payload.getUserId());
        assertEquals(codeChallenge, payload.getCodeChallenge());
        assertEquals(codeChallengeMethod, payload.getCodeChallengeMethod());
        assertEquals(scope, payload.getScope());
    }

    @Test
    void createOauthInfoWithDefaultCodeChallengeMethod() {
        // given
        codeChallengeMethod = null;

        // when
        OauthInfo oauthInfo = OauthInfo.of(clientId, redirectUri, codeChallenge, codeChallengeMethod, scope, responseType);

        // then
        assertEquals("S256", oauthInfo.getCodeChallengeMethod());
    }
}