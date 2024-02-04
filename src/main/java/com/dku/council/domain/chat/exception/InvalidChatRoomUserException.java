package com.dku.council.domain.chat.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidChatRoomUserException extends LocalizedMessageException {
    public InvalidChatRoomUserException() { super(HttpStatus.BAD_REQUEST, "notfound.chat-room-user"); }
}
