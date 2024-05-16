package com.dku.council.domain.oauth.service;

import com.dku.council.domain.oauth.exception.InvalidGrantTypeException;
import com.dku.council.domain.oauth.exception.InvalidOauthResponseTypeException;
import com.dku.council.domain.oauth.exception.OauthCacheNotFoundException;
import com.dku.council.domain.oauth.exception.OauthClientNotFoundException;
import com.dku.council.domain.oauth.model.dto.request.*;
import com.dku.council.domain.oauth.model.dto.response.TokenExchangeResponse;
import com.dku.council.domain.oauth.model.entity.HashAlgorithm;
import com.dku.council.domain.oauth.model.entity.OauthClient;
import com.dku.council.domain.oauth.model.entity.OauthResponseType;
import com.dku.council.domain.oauth.repository.OauthClientRepository;
import com.dku.council.domain.oauth.repository.OauthRedisRepository;
import com.dku.council.domain.oauth.util.CodeChallengeConverter;
import com.dku.council.domain.user.exception.WrongPasswordException;
import com.dku.council.domain.user.model.dto.request.RequestLoginDto;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.user.util.CodeGenerator;
import com.dku.council.global.auth.jwt.AuthenticationToken;
import com.dku.council.global.auth.jwt.JwtProvider;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {
    private final OauthClientRepository oauthClientRepository;
    private final OauthRedisRepository oauthRedisRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeChallengeConverter codeChallengeConverter;
    private final JwtProvider jwtProvider;
    private static final String LOGIN_URL = "https://oauth.danvery.com/signin";

    public String authorize(OauthRequest oauthRequest) {
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectUri();
        checkResponseType(oauthRequest.getResponseType());
        OauthClient oauthClient = getOauthClient(clientId);
        oauthClient.checkClientId(clientId);
        oauthClient.checkRedirectUri(redirectUri);
        return UriComponentsBuilder
                .fromUriString(LOGIN_URL)
                .queryParams(oauthRequest.toQueryParams())
                .toUriString();
    }

    @Transactional
    public String login(RequestLoginDto loginInfo, OauthInfo oauthInfo) {
        checkResponseType(oauthInfo.getResponseType());
        User user = userRepository.findByStudentId(loginInfo.getStudentId())
                .orElseThrow(UserNotFoundException::new);

        checkPassword(loginInfo.getPassword(), user.getPassword());
        String authCode = CodeGenerator.generateUUIDCode();
        Long userId = user.getId();
        OauthCachePayload cachePayload = oauthInfo.toCachePayload(userId);
        oauthRedisRepository.cacheOauth(authCode, cachePayload);
        return UriComponentsBuilder
                .fromUriString(oauthInfo.getRedirectUri())
                .queryParam("code", authCode)
                .toUriString();
    }

    private void checkPassword(String inputPassword, String userPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new WrongPasswordException();
        }
    }

    public TokenExchangeResponse exchangeToken(ClientInfo clientInfo, OAuthTarget target) {
        checkGrantType(target.getGrantType());
        OauthClient oauthClient = getOauthClient(clientInfo.getClientId());
        oauthClient.checkClientSecret(clientInfo.getClientSecret());
        oauthClient.checkRedirectUri(clientInfo.getRedirectUri());
        OauthCachePayload payload = getPayload(target);
        String codeVerifier = target.getCodeVerifier();
        String codeChallengeMethod = getCodeChallengeMethod(payload.getCodeChallengeMethod());
        checkCodeChallenge(codeVerifier, codeChallengeMethod, payload);
        User user = userRepository.findById(payload.getUserId())
                .orElseThrow(UserNotFoundException::new);
        AuthenticationToken token = jwtProvider.issue(user);
        return TokenExchangeResponse.of(token.getAccessToken(), token.getRefreshToken(), payload.getScope());
    }

    private String getCodeChallengeMethod(String codeChallengeMethod) {
        if (codeChallengeMethod == null) {
            return HashAlgorithm.SHA256.getAlgorithm();
        }
        if (Objects.equals(codeChallengeMethod, HashAlgorithm.SHA1.getShortenedAlgorithm())) {
            codeChallengeMethod = HashAlgorithm.SHA1.getAlgorithm();
        } else if (Objects.equals(codeChallengeMethod, HashAlgorithm.SHA256.getShortenedAlgorithm())) {
            codeChallengeMethod = HashAlgorithm.SHA256.getAlgorithm();
        } else if (Objects.equals(codeChallengeMethod, HashAlgorithm.SHA512.getShortenedAlgorithm())) {
            codeChallengeMethod = HashAlgorithm.SHA512.getAlgorithm();
        }
        return codeChallengeMethod;
    }

    private void checkCodeChallenge(String codeVerifier, String codeChallengeMethod, OauthCachePayload payload) {
        String convertedCode = codeChallengeConverter.convertToCodeChallenge(codeVerifier, codeChallengeMethod);
        payload.checkCodeChallenge(convertedCode);
    }

    private OauthCachePayload getPayload(OAuthTarget target) {
        return oauthRedisRepository.getOauth(target.getCode())
                .orElseThrow(OauthCacheNotFoundException::new);
    }

    private OauthClient getOauthClient(String clientId) {
        return oauthClientRepository.findByClientId(clientId)
                .orElseThrow(OauthClientNotFoundException::new);
    }

    private void checkResponseType(String responseType) {
        String type = OauthResponseType.CODE.getValue();
        if (!responseType.equals(type)) {
            throw new InvalidOauthResponseTypeException(responseType);
        }
    }

    private void checkGrantType(String grantType) {
        if (!grantType.equals("authorization_code")) {
            throw new InvalidGrantTypeException(grantType);
        }
    }

}
