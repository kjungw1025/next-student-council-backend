package com.dku.council.domain.banner.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class BannerNotFoundException extends LocalizedMessageException {

    public BannerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.banner");
    }
}
