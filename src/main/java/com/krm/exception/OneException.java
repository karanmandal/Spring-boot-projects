package com.krm.exception;

public class OneException extends RuntimeException {

    private final String reason;

    public OneException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
