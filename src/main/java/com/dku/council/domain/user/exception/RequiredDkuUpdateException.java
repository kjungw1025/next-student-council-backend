package com.dku.council.domain.user.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RequiredDkuUpdateException extends LocalizedMessageException {

    public RequiredDkuUpdateException() {
        super(HttpStatus.NOT_ACCEPTABLE, "required.dku-update");
    }
}
