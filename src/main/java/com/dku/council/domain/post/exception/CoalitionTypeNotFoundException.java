package com.dku.council.domain.post.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class CoalitionTypeNotFoundException extends LocalizedMessageException {

    public CoalitionTypeNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "required.coalition-type");
    }

}
