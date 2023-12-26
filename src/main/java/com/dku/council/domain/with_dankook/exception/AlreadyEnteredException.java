package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyEnteredException extends LocalizedMessageException {

    public AlreadyEnteredException() {
        super(HttpStatus.BAD_REQUEST, "already.entered");
    }
}
