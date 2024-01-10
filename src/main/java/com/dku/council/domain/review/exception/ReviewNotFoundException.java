package com.dku.council.domain.review.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends LocalizedMessageException {
    public ReviewNotFoundException() { super(HttpStatus.NOT_FOUND, "notfound.review"); }
}
