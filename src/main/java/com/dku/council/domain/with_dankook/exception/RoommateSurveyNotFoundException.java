package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RoommateSurveyNotFoundException extends LocalizedMessageException {

    public RoommateSurveyNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.roommate-survey");
    }
}
