package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadySurveyException extends LocalizedMessageException {

    public AlreadySurveyException() {
        super(HttpStatus.BAD_REQUEST, "already.survey");
    }
}
