package com.krm.exception;

import static com.krm.contants.ResponseMessage.EXCEPTION_USERNAME_NOT_FOUND;

public class InvalidUserException extends OneException {

    public InvalidUserException() {
        super("bad_request", EXCEPTION_USERNAME_NOT_FOUND);
    }
}
