package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyFullRecruitedException extends LocalizedMessageException {

    public AlreadyFullRecruitedException() {
        super(HttpStatus.BAD_REQUEST, "already.full-recruited");
    }
}
