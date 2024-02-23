package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidCodeChallengeException extends LocalizedMessageException {
    public InvalidCodeChallengeException(String code) {
        super(HttpStatus.BAD_REQUEST, "invalid.code-challenge: " + code);
    }
}
