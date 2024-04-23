package com.freeuni.macs.authservice.exception;

import com.freeuni.macs.authservice.util.MessageFormatter;

public class UserAuthException extends RuntimeException {

    public UserAuthException(String message, Object... args) {
        super(MessageFormatter.format(message, args));
    }
}
