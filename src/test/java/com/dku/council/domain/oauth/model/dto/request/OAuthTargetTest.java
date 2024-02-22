package com.dku.council.domain.oauth.model.dto.request;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class OAuthTargetTest {

    @Test
    void createOAuthTarget() {
        String grantType = "authorization_code";
        String code = "code";
        String codeVerifier = "codeVerifier";

        OAuthTarget target = OAuthTarget.of(grantType, code, codeVerifier);

        assertEquals(grantType, target.getGrantType());
        assertEquals(code, target.getCode());
        assertEquals(codeVerifier, target.getCodeVerifier());
    }
}