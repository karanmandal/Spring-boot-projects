package com.krm.exception;

import static com.krm.contants.ResponseMessage.EXCEPTION_BAD_MESSAGE;

public class BadMessageException extends OneException {

    public BadMessageException() {
        super("bad_message", EXCEPTION_BAD_MESSAGE);
    }

    public BadMessageException(String reason, String message) {
        super(reason, message);
    }

    public BadMessageException(String message) {
        super("bad_message", message);
    }

}
