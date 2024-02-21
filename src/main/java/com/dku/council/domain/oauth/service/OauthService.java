package com.dku.council.domain.oauth.service;

import com.dku.council.domain.oauth.exception.InvalidOauthResponseTypeException;
import com.dku.council.domain.oauth.exception.OauthClientNotFoundException;
import com.dku.council.domain.oauth.model.dto.request.OauthCachePayload;
import com.dku.council.domain.oauth.model.dto.request.OauthRequest;
import com.dku.council.domain.oauth.model.entity.OauthClient;
import com.dku.council.domain.oauth.model.entity.OauthResponseType;
import com.dku.council.domain.oauth.repository.OauthClientRepository;
import com.dku.council.domain.oauth.repository.OauthRedisRepository;
import com.dku.council.domain.user.exception.WrongPasswordException;
import com.dku.council.domain.user.model.dto.request.RequestLoginDto;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.user.util.CodeGenerator;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String authorize(OauthRequest oauthRequest) {
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectUri();
        checkResponseType(oauthRequest.getResponseType());
        OauthClient oauthClient = getOauthClient(clientId);
        oauthClient.checkClientId(clientId);
        oauthClient.checkRedirectUri(redirectUri);
        return UriComponentsBuilder
                .fromUriString(oauthClient.getRedirectUri())
                .toUriString();
    }

    @Transactional
    public OauthLoginResponse login(RequestLoginDto loginInfo, OauthInfo oauthInfo) {
        checkResponseType(oauthInfo.getResponseType());
        User user = userRepository.findByStudentId(loginInfo.getStudentId())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        String authCode = CodeGenerator.generateUUIDCode();
        Long userId = user.getId();
        OauthCachePayload cachePayload = oauthInfo.toCachePayload(userId);
        oauthRedisRepository.cacheOauth(authCode, cachePayload);
        return OauthLoginResponse.from(authCode);
    }
    private void checkResponseType(String responseType) {
        String type = OauthResponseType.CODE.getValue();
        if (!responseType.equals(type)) {
            throw new InvalidOauthResponseTypeException(responseType);
        }
    }
}
