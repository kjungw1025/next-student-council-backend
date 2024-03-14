package com.dku.council.domain.danfesta.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class NotFoundFestivalDateException extends LocalizedMessageException {

    public NotFoundFestivalDateException() {
        super(HttpStatus.NOT_FOUND, "notfound.festival-date");
    }
}
