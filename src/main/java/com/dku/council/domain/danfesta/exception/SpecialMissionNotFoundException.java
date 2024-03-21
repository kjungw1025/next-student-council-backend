package com.dku.council.domain.danfesta.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class SpecialMissionNotFoundException extends LocalizedMessageException {

    public SpecialMissionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.special-mission");
    }
}
