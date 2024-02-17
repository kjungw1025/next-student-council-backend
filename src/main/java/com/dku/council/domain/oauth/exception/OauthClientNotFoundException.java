package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class OauthClientNotFoundException extends LocalizedMessageException {
    public OauthClientNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.oauth-client");
    }
}
