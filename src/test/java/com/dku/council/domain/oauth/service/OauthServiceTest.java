package com.dku.council.domain.oauth.service;

import com.dku.council.domain.oauth.exception.InvalidOauthResponseTypeException;
import com.dku.council.domain.oauth.exception.OauthClientNotFoundException;
import com.dku.council.domain.oauth.model.dto.request.OauthRequest;
import com.dku.council.domain.oauth.repository.OauthClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OauthServiceTest {
    @InjectMocks
    private OauthService oauthService;

    @Mock
    private OauthClientRepository oauthClientRepository;

    @Test
    @DisplayName("responseType이 올바르지 않은 경우")
    void checkInvalidResponseType() {
        // given
        String codeChallenge = "codeChallenge";
        String codeChallengeMethod = "S256";
        String clientId = "clientId";
        String redirectUri = "http://localhost:8080/oauth/redirect";
        String responseType = "not_code";
        String scope = "nickname email";
        OauthRequest request = OauthRequest.of(codeChallenge, codeChallengeMethod, clientId,
                redirectUri, responseType, scope);

        // when, then
        assertThrows(InvalidOauthResponseTypeException.class, () -> {
            oauthService.authorize(request);
        }, "Invalid.oauth-response-type: not_code");
    }

    @Test
    @DisplayName("해당 clientId가 존재하지 않는 경우")
    void checkClientIdNotExist() {
        // given
        String codeChallenge = "codeChallenge";
        String codeChallengeMethod = "S256";
        String clientId = "notExistClientId";
        String redirectUri = "http://localhost:8080/oauth/redirect";
        String responseType = "code";
        String scope = "nickname email";
        OauthRequest request = OauthRequest.of(codeChallenge, codeChallengeMethod, clientId,
                redirectUri, responseType, scope);

        // when, then
        assertThrows(OauthClientNotFoundException.class, () -> {
            oauthService.authorize(request);
        }, "notfound.oauth-client: notExistClientId");
    }
}