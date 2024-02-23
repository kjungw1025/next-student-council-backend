package com.dku.council.domain.oauth.model.entity;

import com.dku.council.domain.oauth.exception.InvalidOauthClientIdException;
import com.dku.council.domain.oauth.exception.InvalidOauthRedirectUriException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OauthClientTest {

    @Test
    void testGetClientId() {
        String clientId = "id";
        String applicationName = "tm";
        String clientSecret = "cs";
        String redirectUri = "url";
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri);
        assertEquals("id", oauthClient.getClientId());
    }

    @Test
    void testGetClientSecret() {
        String clientId = "id";
        String applicationName = "tm";
        String clientSecret = "cs";
        String redirectUri = "url";
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri);
        assertEquals("cs", oauthClient.getClientSecret());
    }

    @Test
    void testGetRedirectUri() {
        String clientId = "id";
        String applicationName = "tm";
        String clientSecret = "cs";
        String redirectUri = "url";
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri);
        assertEquals("url", oauthClient.getRedirectUri());
    }

    @Test
    void testStaticOf() {
        String clientId = "id";
        String applicationName = "tm";
        String clientSecret = "cs";
        String redirectUri = "url";
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri);
        assertEquals("id", oauthClient.getClientId());
        assertEquals("tm", oauthClient.getApplicationName());
        assertEquals("cs", oauthClient.getClientSecret());
        assertEquals("url", oauthClient.getRedirectUri());
    }

    @Test
    void testCheckClientId() {
        String clientId = "id";
        String applicationName = "tm";
        String clientSecret = "cs";
        String redirectUri = "url";
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri);
        assertThrows(InvalidOauthClientIdException.class,
                () -> oauthClient.checkClientId("invalidId"), "invalid.oauth-client-id: invalidId");
    }

    @Test
    void testCheckClientSecret() {
        String clientId = "id";
        String applicationName = "tm";
        String clientSecret = "cs";
        String redirectUri = "url";
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri);
        assertThrows(IllegalArgumentException.class,
                () -> oauthClient.checkClientSecret("invalidCs"), "Invalid client secret");
    }

    @Test
    void testCheckRedirectUri() {
        String clientId = "id";
        String applicationName = "tm";
        String clientSecret = "cs";
        String redirectUri = "url";
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri);
        assertThrows(InvalidOauthRedirectUriException.class,
                () -> oauthClient.checkRedirectUri("invalidUrl"), "invalid.oauth-redirect-uri: invalidUrl");
    }
}
