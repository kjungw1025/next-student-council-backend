package com.dku.council.domain.oauth.model.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientInfoTest {
    @Test
    void createClientInfoWithGivenParameters() {
        // given
        String clientId = "clientId";
        String clientSecret = "clientSecret";
        String redirectUri = "redirectUri";

        // when
        ClientInfo clientInfo = ClientInfo.of(clientId, clientSecret, redirectUri);

        // then
        assertEquals(clientId, clientInfo.getClientId());
        assertEquals(clientSecret, clientInfo.getClientSecret());
        assertEquals(redirectUri, clientInfo.getRedirectUri());
    }

}