package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidMBTIException extends LocalizedMessageException {

    public InvalidMBTIException() {
        super(HttpStatus.BAD_REQUEST, "invalid.mbti");
    }
}
