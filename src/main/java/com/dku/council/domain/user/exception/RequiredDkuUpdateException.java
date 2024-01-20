package com.dku.council.domain.user.exception;

import com.dku.council.global.error.CustomHttpStatus;
import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RequiredDkuUpdateException extends LocalizedMessageException {

    public RequiredDkuUpdateException() {
        super(CustomHttpStatus.REQUIRED_DKU_UPDATE, "required.dku-update");
    }
}
