package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class TradeCooltimeException extends LocalizedMessageException {
    public TradeCooltimeException(String withDankookType) {
        super(HttpStatus.BAD_REQUEST, "cooltime." + withDankookType);
    }
}
