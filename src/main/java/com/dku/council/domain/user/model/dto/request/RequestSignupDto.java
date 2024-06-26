package com.dku.council.domain.user.model.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class RequestSignupDto {

    @NotBlank
    @Size(min = 3, max = 16)
    @Pattern(regexp = "^(?!.*\\s{2,})[A-Za-z\\dㄱ-ㅎㅏ-ㅣ가-힣_ ]{3,16}$")
    private final String nickname;

    @NotBlank
    @Size(min = 3, max = 200)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")
    private final String password;
}
