package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidMinStudentIdException extends LocalizedMessageException {

    public InvalidMinStudentIdException() {
        super(HttpStatus.BAD_REQUEST, "invalid.student-id");
    }
}
