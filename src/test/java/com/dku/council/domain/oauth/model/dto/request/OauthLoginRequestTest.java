package com.dku.council.domain.oauth.model.dto.request;

import com.dku.council.domain.user.model.dto.request.RequestLoginDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OauthLoginRequestTest {
    private static Validator validator;
    private static String studentId;
    private static String password;
    private static String clientId;
    private static String redirectUri;
    private static String codeChallenge;
    private static String codeChallengeMethod;
    private static String scope;
    private static String responseType;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        studentId = "studentId";
        password = "password";
        clientId = "clientId";
        redirectUri = "https://redirectUri.com";
        codeChallenge = "codeChallenge";
        codeChallengeMethod = "S256";
        scope = "scope";
        responseType = "code";
    }

    @Test
    void createOauthLoginRequestWithGivenParameters() {
        //when
        OauthLoginRequest request = new OauthLoginRequest(studentId, password, clientId,
                redirectUri, codeChallenge, codeChallengeMethod, scope, responseType);

        //then
        assertEquals(studentId, request.getStudentId());
        assertEquals(password, request.getPassword());
        assertEquals(clientId, request.getClientId());
        assertEquals(redirectUri, request.getRedirectUri());
        assertEquals(codeChallenge, request.getCodeChallenge());
        assertEquals(codeChallengeMethod, request.getCodeChallengeMethod());
        assertEquals(scope, request.getScope());
        assertEquals(responseType, request.getResponseType());
    }

    @Test
    void convertToLoginInfo() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(studentId, password, null, null,
                null, null, null, null);

        //when
        RequestLoginDto loginInfo = request.toLoginInfo();

        //then
        assertEquals(studentId, loginInfo.getStudentId());
        assertEquals(password, loginInfo.getPassword());
    }

    @Test
    void validateStudentIdIsBlank() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(" ", password, clientId, redirectUri,
                codeChallenge, codeChallengeMethod, scope, responseType);

        //when
        Set<ConstraintViolation<OauthLoginRequest>> violations = validator.validate(request);

        //then
        assertEquals(1, violations.size());
        assertEquals("학번을 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validatePasswordIsBlank() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(studentId, " ", clientId, redirectUri,
                codeChallenge, codeChallengeMethod, scope, responseType);

        //when
        Set<ConstraintViolation<OauthLoginRequest>> violations = validator.validate(request);

        //then
        assertEquals(1, violations.size());
        assertEquals("비밀번호를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateClientIdIsBlank() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(studentId, password, " ", redirectUri,
                codeChallenge, codeChallengeMethod, scope, responseType);

        //when
        Set<ConstraintViolation<OauthLoginRequest>> violations = validator.validate(request);

        //then
        assertEquals(1, violations.size());
        assertEquals("clientId를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateRedirectUriIsBlank() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(studentId, password, clientId, " ",
                codeChallenge, codeChallengeMethod, scope, responseType);

        //when
        Set<ConstraintViolation<OauthLoginRequest>> violations = validator.validate(request);

        //then
        assertEquals(1, violations.size());
        assertEquals("redirectUri를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateCodeChallengeIsBlank() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(studentId, password, clientId, redirectUri,
                " ", codeChallengeMethod, scope, responseType);

        //when
        Set<ConstraintViolation<OauthLoginRequest>> violations = validator.validate(request);

        //then
        assertEquals(1, violations.size());
        assertEquals("codeChallenge를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateScopeIsBlank() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(studentId, password, clientId, redirectUri,
                codeChallenge, codeChallengeMethod, " ", responseType);

        //when
        Set<ConstraintViolation<OauthLoginRequest>> violations = validator.validate(request);

        //then
        assertEquals(1, violations.size());
        assertEquals("scope를 입력해주세요.", violations.iterator().next().getMessage());
    }

    @Test
    void validateResponseTypeIsBlank() {
        //given
        OauthLoginRequest request = new OauthLoginRequest(studentId, password, clientId, redirectUri,
                codeChallenge, codeChallengeMethod, scope, " ");

        //when
        Set<ConstraintViolation<OauthLoginRequest>> violations = validator.validate(request);

        //then
        assertEquals(1, violations.size());
        assertEquals("responseType을 입력해주세요.", violations.iterator().next().getMessage());
    }
}
