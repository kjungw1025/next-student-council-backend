package com.dku.council.domain.chat.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends LocalizedMessageException {
    public ChatRoomNotFoundException() { super(HttpStatus.NOT_FOUND, "notfound.chat-room"); }
}