package com.dku.council.domain.oauth.model.dto.request;

import com.dku.council.domain.user.model.dto.request.RequestLoginDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class OauthLoginRequest {
    @NotBlank
    @Schema(description = "아이디(학번)", example = "12345678")
    private final String studentId;

    @NotBlank
    @Schema(description = "비밀번호", example = "121212")
    private final String password;

    private final String clientId;

    private final String redirectUri;

    private final String codeChallenge;

    private final String codeChallengeMethod;

    private final String scope;

    private final String responseType;

    public RequestLoginDto toLoginInfo() {
        return new RequestLoginDto(studentId, password);
    }

    public OauthInfo toOauthInfo() {
        return OauthInfo.of(clientId, redirectUri, codeChallenge, codeChallengeMethod, scope,  responseType);
    }
}
