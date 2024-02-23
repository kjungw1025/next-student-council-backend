package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidOauthResponseTypeException extends LocalizedMessageException {
    public InvalidOauthResponseTypeException(String responseType) {
        super(HttpStatus.BAD_REQUEST, "invalid.oauth-response-type", responseType);
    }
}
