package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class ImageSizeExceededException extends LocalizedMessageException {

    public ImageSizeExceededException() {
        super(HttpStatus.BAD_REQUEST, "invalid.imagesize");
    }
}
