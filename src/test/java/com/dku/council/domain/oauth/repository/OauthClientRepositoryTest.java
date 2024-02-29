package com.dku.council.domain.oauth.repository;

import com.dku.council.domain.oauth.model.entity.OauthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OauthClientRepositoryTest {

    @Autowired
    private OauthClientRepository oauthClientRepository;

    private String clientId;
    private String appName;
    private String clientSecret;
    private String redirectUri;

    @BeforeEach
    void setUp() {
        clientId = "clientId";
        appName = "appName";
        clientSecret = "clientSecret";
        redirectUri = "redirectUri";
    }

    @Test
    void saveOauthClient() {
        OauthClient oauthClient = OauthClient.of(clientId, appName, clientSecret, redirectUri);

        OauthClient savedOauthClient = oauthClientRepository.save(oauthClient);

        assertEquals(clientId, savedOauthClient.getClientId());
        assertEquals(appName, savedOauthClient.getApplicationName());
        assertEquals(clientSecret, savedOauthClient.getClientSecret());
        assertEquals(redirectUri, savedOauthClient.getRedirectUri());
    }

    @Test
    void findOauthClient() {
        OauthClient oauthClient = OauthClient.of(clientId, appName, clientSecret, redirectUri);
        oauthClientRepository.save(oauthClient);

        Optional<OauthClient> foundOauthClient = oauthClientRepository.findByClientId(clientId);

        assertTrue(foundOauthClient.isPresent());
        OauthClient client = foundOauthClient.get();
        assertEquals(clientId, client.getClientId());
        assertEquals(appName, client.getApplicationName());
        assertEquals(clientSecret, client.getClientSecret());
        assertEquals(redirectUri, client.getRedirectUri());
    }
}
