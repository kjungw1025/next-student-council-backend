package com.dku.council.domain.oauth.model.dto.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OauthRequestTest {

    private static Validator validator;
    private static String codeChallenge;
    private static String clientId;
    private static String redirectUri;
    private static String responseType;
    private static String scope;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        codeChallenge = "codeChallenge";
        clientId = "clientId";
        redirectUri = "https://redirectUri.com";
        responseType = "code";
        scope = "scope";
    }

    @Test
    void createOauthRequestWithGivenParameters() {
        String codeChallengeMethod = "S256";

        OauthRequest request = OauthRequest.of(codeChallenge, codeChallengeMethod, clientId, redirectUri, responseType, scope);

        assertEquals(codeChallenge, request.getCodeChallenge());
        assertEquals(codeChallengeMethod, request.getCodeChallengeMethod());
        assertEquals(clientId, request.getClientId());
        assertEquals(redirectUri, request.getRedirectUri());
        assertEquals(responseType, request.getResponseType());
        assertEquals(scope, request.getScope());
    }

    @Test
    void createOauthRequestWithDefaultCodeChallengeMethod() {
        OauthRequest request = OauthRequest.of(codeChallenge, clientId, redirectUri, responseType, scope);

        assertEquals(codeChallenge, request.getCodeChallenge());
        assertEquals("S256", request.getCodeChallengeMethod());
        assertEquals(clientId, request.getClientId());
        assertEquals(redirectUri, request.getRedirectUri());
        assertEquals(responseType, request.getResponseType());
        assertEquals(scope, request.getScope());
    }

    @Test
    void throwExceptionWhenCodeChallengeIsBlank() {
        OauthRequest request = OauthRequest.of(" ", clientId, redirectUri, responseType, scope);

        Set<ConstraintViolation<OauthRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }

    @Test
    void throwExceptionWhenClientIdIsBlank() {
        OauthRequest request = OauthRequest.of(codeChallenge, " ", redirectUri, responseType, scope);

        Set<ConstraintViolation<OauthRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }

    @Test
    void throwExceptionWhenRedirectUriIsBlank() {
        OauthRequest request = OauthRequest.of(codeChallenge, clientId, " ", responseType, scope);

        Set<ConstraintViolation<OauthRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }

    @Test
    void throwExceptionWhenResponseTypeIsBlank() {
        OauthRequest request = OauthRequest.of(codeChallenge, clientId, redirectUri, " ", scope);

        Set<ConstraintViolation<OauthRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }

    @Test
    void throwExceptionWhenScopeIsBlank() {
        OauthRequest request = OauthRequest.of(codeChallenge, clientId, redirectUri, responseType, " ");

        Set<ConstraintViolation<OauthRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }
}