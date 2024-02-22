package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class OauthCacheNotFoundException extends LocalizedMessageException {
    public OauthCacheNotFoundException() {
        super(HttpStatus.NOT_FOUND, "oauth-cache-not-found");
    }
}
