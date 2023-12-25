package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class WithDankookNotFoundException extends LocalizedMessageException {

    public WithDankookNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.withdankook");
    }
}
