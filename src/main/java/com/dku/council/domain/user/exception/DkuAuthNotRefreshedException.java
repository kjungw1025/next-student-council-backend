package com.dku.council.domain.user.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class DkuAuthNotRefreshedException extends LocalizedMessageException {

    public DkuAuthNotRefreshedException() {
        super(HttpStatus.BAD_REQUEST, "invalid.dku-auth.refresh");
    }
}
