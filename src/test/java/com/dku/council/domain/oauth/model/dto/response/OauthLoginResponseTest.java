package com.dku.council.domain.oauth.model.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OauthLoginResponseTest {

    @Test
    void createOauthLoginResponseWithGivenAuthCode() {
        String authCode = "authCode";
        OauthLoginResponse response = OauthLoginResponse.from(authCode);
        assertEquals(authCode, response.getAuthCode());
    }
}
