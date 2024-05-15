package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidGrantTypeException extends LocalizedMessageException {
    public InvalidGrantTypeException(String grantType) {
        super(HttpStatus.NOT_ACCEPTABLE, "invalid.oauth-grant-type: " + grantType);
    }
}
