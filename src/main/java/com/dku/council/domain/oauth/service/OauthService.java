package com.dku.council.domain.oauth.service;

import com.dku.council.domain.oauth.exception.InvalidOauthResponseTypeException;
import com.dku.council.domain.oauth.exception.OauthClientNotFoundException;
import com.dku.council.domain.oauth.model.dto.request.OauthCachePayload;
import com.dku.council.domain.oauth.model.dto.request.OauthRequest;
import com.dku.council.domain.oauth.model.entity.OauthClient;
import com.dku.council.domain.oauth.model.entity.OauthResponseType;
import com.dku.council.domain.oauth.repository.OauthClientRepository;
import com.dku.council.domain.oauth.repository.OauthRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {
    private final OauthClientRepository oauthClientRepository;
    private final OauthRedisRepository oauthRedisRepository;

    @Transactional
    public String authorize(OauthRequest authRequest) {
        String clientId = authRequest.getClientId();
        String redirectUri = authRequest.getRedirectUri();
        checkResponseType(authRequest.getResponseType());
        OauthClient oauthClient = oauthClientRepository.findByClientId(clientId)
                .orElseThrow(OauthClientNotFoundException::new);
        oauthClient.checkClientId(clientId);
        oauthClient.checkRedirectUri(redirectUri);
        String authCode = UUID.randomUUID().toString();
        OauthCachePayload cachePayload = authRequest.toCachePayload(authCode);
        oauthRedisRepository.cacheOauth(clientId, cachePayload);
        return UriComponentsBuilder
                .fromUriString(oauthClient.getRedirectUri())
                .queryParam("code", authCode)
                .toUriString();
    }

    private void checkResponseType(String responseType) {
        String type = OauthResponseType.CODE.getValue();
        if (!responseType.equals(type)) {
            throw new InvalidOauthResponseTypeException(responseType);
        }
    }
}
