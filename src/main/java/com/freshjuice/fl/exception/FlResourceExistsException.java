package com.freshjuice.fl.exception;

public class FlResourceExistsException extends RuntimeException {

    public FlResourceExistsException(String message) {
        super(message);
    }

    public FlResourceExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlResourceExistsException(Throwable cause) {
        super(cause);
    }

    public FlResourceExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
