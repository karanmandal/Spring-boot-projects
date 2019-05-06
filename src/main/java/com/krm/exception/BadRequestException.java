package com.krm.exception;

import static com.krm.contants.ResponseMessage.EXCEPTION_BAD_REQUEST;

public class BadRequestException extends OneException {

    public BadRequestException() {
        super("bad_request", EXCEPTION_BAD_REQUEST);
    }

    public BadRequestException(String reason, String message) {
        super(reason, message);
    }

    public BadRequestException(String message) {
        super("bad_request", message);
    }

}
