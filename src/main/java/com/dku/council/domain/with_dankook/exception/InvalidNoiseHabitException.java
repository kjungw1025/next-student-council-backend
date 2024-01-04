package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidNoiseHabitException extends LocalizedMessageException {

    public InvalidNoiseHabitException() {
        super(HttpStatus.BAD_REQUEST, "required.noise-habit");
    }
}
