package com.dku.council.domain.review.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidCreateReviewToMyselfException extends LocalizedMessageException {
    public InvalidCreateReviewToMyselfException() { super(HttpStatus.BAD_REQUEST, "invalid.create-review-to-myself"); }
}
