package com.dku.council.domain.oauth.model.dto.request;

import com.dku.council.domain.user.model.dto.request.RequestLoginDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class OauthLoginRequest {
    @NotBlank(message = "학번을 입력해주세요.")
    @Schema(description = "아이디(학번)", example = "12345678")
    private final String studentId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "비밀번호", example = "121212")
    private final String password;

    @NotBlank(message = "clientId를 입력해주세요.")
    private final String clientId;

    @NotBlank(message = "redirectUri를 입력해주세요.")
    private final String redirectUri;

    @NotBlank(message = "codeChallenge를 입력해주세요.")
    private final String codeChallenge;

    @Nullable
    private final String codeChallengeMethod;

    @NotBlank(message = "scope를 입력해주세요.")
    private final String scope;

    @NotBlank(message = "responseType을 입력해주세요.")
    private final String responseType;

    public RequestLoginDto toLoginInfo() {
        return new RequestLoginDto(studentId, password);
    }

    public OauthInfo toOauthInfo() {
        return OauthInfo.of(clientId, redirectUri, codeChallenge, codeChallengeMethod, scope, responseType);
    }
}
