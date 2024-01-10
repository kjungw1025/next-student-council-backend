package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class StudyCooltimeException extends LocalizedMessageException {
    public StudyCooltimeException(String withDankookType) {
        super(HttpStatus.BAD_REQUEST, "cooltime." + withDankookType);
    }
}
