package com.dku.council.domain.oauth.model.entity;

import com.dku.council.domain.oauth.exception.InvalidOauthClientIdException;
import com.dku.council.domain.oauth.exception.InvalidOauthRedirectUriException;
import com.dku.council.domain.oauth.exception.InvalidClientSecretException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.HashSet;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OauthClient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "oauth_client_id")
    private Long id;
    private String clientId;
    private String applicationName;
    private String clientSecret;
    private String redirectUri;

    private OauthClient(String clientId, String applicationName, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.applicationName = applicationName;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public static OauthClient of(String clientId, String applicationName, String clientSecret, String redirectUri) {
        return new OauthClient(clientId, applicationName, clientSecret, redirectUri);
    }

    public void checkClientId(String clientId) {
        if (!this.clientId.equals(clientId)) {
            throw new InvalidOauthClientIdException(clientId);
        }
    }

    public void checkClientSecret(String clientSecret) {
        if (!this.clientSecret.equals(clientSecret)) {
            throw new InvalidClientSecretException(clientSecret);
        }
    }

    public void checkRedirectUri(String redirectUri) {
        HashSet<String> redirectUriSet = new HashSet<>(List.of(this.redirectUri.split(" ")));
        if (!redirectUriSet.contains(redirectUri)) {
            throw new InvalidOauthRedirectUriException(redirectUri);
        }
    }
}
