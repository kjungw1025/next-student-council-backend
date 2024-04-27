package com.dku.council.domain.banner.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidBannerTypeException extends LocalizedMessageException {
    public InvalidBannerTypeException(String imageName) {
        super(HttpStatus.BAD_REQUEST, "invalid.banner-type", imageName);
    }
}
