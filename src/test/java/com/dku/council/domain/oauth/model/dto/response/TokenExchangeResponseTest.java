package com.dku.council.domain.oauth.model.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenExchangeResponseTest {

    @Test
    void createTokenExchangeResponse() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String scope = "name studentId";
        TokenExchangeResponse response = TokenExchangeResponse.of(accessToken, refreshToken, scope);

        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals(scope, response.getScope());
        assertEquals("Bearer", response.getTokenType());
    }
}