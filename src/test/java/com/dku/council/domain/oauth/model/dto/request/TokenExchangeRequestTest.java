package com.dku.council.domain.oauth.model.dto.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TokenExchangeRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createTokenExchangeRequestWithGivenParameters() {
        // given
        String grantType = "authorization_code";
        String clientId = "clientId";
        String redirectUri = "redirectUri";
        String clientSecret = "clientSecret";
        String code = "code";
        String codeVerifier = "codeVerifier";

        // when
        TokenExchangeRequest request = TokenExchangeRequest.of(grantType, clientId, redirectUri, clientSecret, code, codeVerifier);

        // then
        assertEquals(grantType, request.getGrantType());
        assertEquals(clientId, request.getClientId());
        assertEquals(redirectUri, request.getRedirectUri());
        assertEquals(clientSecret, request.getClientSecret());
        assertEquals(code, request.getCode());
        assertEquals(codeVerifier, request.getCodeVerifier());
    }

    @Test
    void convertToClientInfo() {
        // given
        String clientId = "clientId";
        String clientSecret = "clientSecret";
        String redirectUri = "redirectUri";
        TokenExchangeRequest request = TokenExchangeRequest.of("authorization_code", clientId,
                redirectUri, clientSecret, "code", "codeVerifier");

        // when
        ClientInfo clientInfo = request.toClientInfo();

        // then
        assertEquals(clientId, clientInfo.getClientId());
        assertEquals(clientSecret, clientInfo.getClientSecret());
        assertEquals(redirectUri, clientInfo.getRedirectUri());
    }

    @Test
    void convertToOAuthTarget() {
        // given
        String grantType = "authorization_code";
        String code = "code";
        String codeVerifier = "codeVerifier";
        TokenExchangeRequest request = TokenExchangeRequest.of(grantType, "clientId",
                "redirectUri", "clientSecret", code, codeVerifier);

        // when
        OAuthTarget authTarget = request.toAuthTarget();

        // then
        assertEquals(grantType, authTarget.getGrantType());
        assertEquals(code, authTarget.getCode());
        assertEquals(codeVerifier, authTarget.getCodeVerifier());
    }

    @Test
    void validateGrantType() {
        // given
        TokenExchangeRequest request = TokenExchangeRequest.of(" ", "clientId",
                "redirectUri", "clientSecret", "code", "codeVerifier");

        // when
        Set<ConstraintViolation<TokenExchangeRequest>> violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        assertEquals("grantType을 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateClientId() {
        // given
        TokenExchangeRequest request = TokenExchangeRequest.of("authorization_code", " ",
                "redirectUri", "clientSecret", "code", "codeVerifier");

        // when
        Set<ConstraintViolation<TokenExchangeRequest>> violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        assertEquals("clientId를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateRedirectUri() {
        // given
        TokenExchangeRequest request = TokenExchangeRequest.of("authorization_code", "clientId",
                " ", "clientSecret", "code", "codeVerifier");

        // when
        Set<ConstraintViolation<TokenExchangeRequest>> violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        assertEquals("redirectUri를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateClientSecret() {
        // given
        TokenExchangeRequest request = TokenExchangeRequest.of("authorization_code", "clientId",
                "redirectUri", " ", "code", "codeVerifier");

        // when
        Set<ConstraintViolation<TokenExchangeRequest>> violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        assertEquals("clientSecret을 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateCode() {
        // given
        TokenExchangeRequest request = TokenExchangeRequest.of("authorization_code", "clientId",
                "redirectUri", "clientSecret", " ", "codeVerifier");

        // when
        Set<ConstraintViolation<TokenExchangeRequest>> violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        assertEquals("code를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateCodeVerifier() {
        // given
        TokenExchangeRequest request = TokenExchangeRequest.of("authorization_code", "clientId",
                "redirectUri", "clientSecret", "code", " ");

        // when
        Set<ConstraintViolation<TokenExchangeRequest>> violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        assertEquals("codeVerifier를 입력해주세요.", violations.iterator().next().getMessage());
    }
}
