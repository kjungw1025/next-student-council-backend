package com.dku.council.domain.danfesta.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class StampNotFoundException extends LocalizedMessageException {

    public StampNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.stamp");
    }
}
