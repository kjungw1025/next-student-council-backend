package com.dku.council.domain.oauth.controller;

import com.dku.council.domain.oauth.model.dto.request.OauthRequest;
import com.dku.council.domain.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/authorize")
    public void authorize(@RequestParam String codeChallenge,
                          @RequestParam(required = false) String codeChallengeMethod,
                          @RequestParam String clientId,
                          @RequestParam String redirectUri,
                          @RequestParam String responseType,
                          @RequestParam String scope,
                          HttpServletResponse response) throws IOException {
        OauthRequest request = OauthRequest.of(codeChallenge, codeChallengeMethod, clientId,
                redirectUri, responseType, scope);
        String uri = oauthService.authorize(request);
        response.sendRedirect(uri);
    }

}
