package com.dku.council.domain.oauth.model.dto.request;

import lombok.Getter;

@Getter
public class ClientInfo {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    private ClientInfo(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public static ClientInfo of(String clientId, String clientSecret, String redirectUri) {
        return new ClientInfo(clientId, clientSecret, redirectUri);
    }
}
