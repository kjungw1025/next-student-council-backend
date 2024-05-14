package com.dku.council.domain.oauth.controller;

import com.dku.council.domain.oauth.model.dto.request.OauthLoginRequest;
import com.dku.council.domain.oauth.model.dto.request.OauthRequest;
import com.dku.council.domain.oauth.model.dto.request.TokenExchangeRequest;
import com.dku.council.domain.oauth.model.dto.response.TokenExchangeResponse;
import com.dku.council.domain.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam String codeChallenge,
                                  @RequestParam(required = false) String codeChallengeMethod,
                                  @RequestParam String clientId,
                                  @RequestParam String redirectUri,
                                  @RequestParam String responseType,
                                  @RequestParam String scope) {
        OauthRequest request = OauthRequest.of(codeChallenge, codeChallengeMethod, clientId,
                redirectUri, responseType, scope);
        String uri = oauthService.authorize(request);
        return new RedirectView(uri);
    }

    @PostMapping("/login")
    public RedirectView login(@RequestBody OauthLoginRequest request) {
        String uri = oauthService.login(request.toLoginInfo(), request.toOauthInfo());
        return new RedirectView(uri);
    }


    @PostMapping("/token")
    public ResponseEntity<TokenExchangeResponse> exchangeToken(@RequestParam String grantType,
                                                               @RequestParam String clientId,
                                                               @RequestParam String redirectUri,
                                                               @RequestParam String clientSecret,
                                                               @RequestParam String code,
                                                               @RequestParam String codeVerifier) {
        TokenExchangeRequest request = TokenExchangeRequest.of(grantType, clientId, redirectUri,
                clientSecret, code, codeVerifier);
        TokenExchangeResponse response = oauthService.exchangeToken(request.toClientInfo(), request.toAuthTarget());
        return ResponseEntity.ok(response);
    }

}
