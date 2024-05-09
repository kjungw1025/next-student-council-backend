package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidClientSecretException extends LocalizedMessageException {
    public InvalidClientSecretException(String clientSecret) {
        super(HttpStatus.BAD_REQUEST, "invalid.oauth-client-secret: " + clientSecret);
    }
}
