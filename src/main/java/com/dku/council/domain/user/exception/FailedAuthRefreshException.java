package com.dku.council.domain.user.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class FailedAuthRefreshException extends LocalizedMessageException {
    public FailedAuthRefreshException() { super(HttpStatus.BAD_REQUEST, "failed.auth-refresh"); }
}
