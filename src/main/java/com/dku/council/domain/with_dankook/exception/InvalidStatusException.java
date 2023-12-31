package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidStatusException extends LocalizedMessageException {

    public InvalidStatusException() {
        super(HttpStatus.BAD_REQUEST, "invalid.status");
    }
}
