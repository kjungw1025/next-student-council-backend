package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidOauthRedirectUriException extends LocalizedMessageException {
    public InvalidOauthRedirectUriException(String redirectUri) {
        super(HttpStatus.NOT_ACCEPTABLE, "invalid.oauth-redirect-uri: ", redirectUri);
    }
}
