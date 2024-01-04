package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyWrittenRoommateException extends LocalizedMessageException {

    public AlreadyWrittenRoommateException() {
        super(HttpStatus.BAD_REQUEST, "already.written.roommate");
    }
}
