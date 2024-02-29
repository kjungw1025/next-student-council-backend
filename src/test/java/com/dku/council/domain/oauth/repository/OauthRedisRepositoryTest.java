package com.dku.council.domain.oauth.repository;

import com.dku.council.domain.oauth.model.dto.request.OauthCachePayload;
import com.dku.council.util.base.AbstractContainerRedisTest;
import com.dku.council.util.test.FullIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@FullIntegrationTest
class OauthRedisRepositoryTest extends AbstractContainerRedisTest {

    @Autowired
    private OauthRedisRepository oauthRedisRepository;

    private String authCode;
    private Long userId;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String scope;

    @BeforeEach
    void setUp() {
        authCode = "authCode";
        userId = 1L;
        codeChallenge = "codeChallenge";
        codeChallengeMethod = "codeChallengeMethod";
        scope = "scope";
    }

    @Test
    void cacheOauth() {
        OauthCachePayload cachePayload = new OauthCachePayload(userId, codeChallenge, codeChallengeMethod, scope);

        oauthRedisRepository.cacheOauth(authCode, cachePayload);

        Optional<OauthCachePayload> payloadOptional = oauthRedisRepository.getOauth(authCode);
        assertTrue(payloadOptional.isPresent());
        OauthCachePayload payload = payloadOptional.get();
        assertEquals(cachePayload.getCodeChallenge(), payload.getCodeChallenge());
        assertEquals(cachePayload.getCodeChallengeMethod(), payload.getCodeChallengeMethod());
        assertEquals(cachePayload.getScope(), payload.getScope());
        assertEquals(cachePayload.getUserId(), payload.getUserId());
    }

    @Test
    void returnNothingWhenAuthCodeNotExist() {
        String nonExistingAuthCode = "nonExistingAuthCode";

        Optional<OauthCachePayload> retrievedPayload = oauthRedisRepository.getOauth(nonExistingAuthCode);

        assertFalse(retrievedPayload.isPresent());
    }

    @Test
    void removeCachedOauth() {
        OauthCachePayload cachePayload = new OauthCachePayload(userId, codeChallenge, codeChallengeMethod, scope);

        oauthRedisRepository.cacheOauth(authCode, cachePayload);
        oauthRedisRepository.deleteOauth(authCode);

        Optional<OauthCachePayload> retrievedPayload = oauthRedisRepository.getOauth(authCode);

        assertFalse(retrievedPayload.isPresent());
    }
}