package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class WithDankookUserNotFoundException extends LocalizedMessageException {
    public WithDankookUserNotFoundException()  {
        super(HttpStatus.NOT_FOUND, "notfound.withdankook-user");
    }
}
