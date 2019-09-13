package com.example.kalah.exceptions;

/**
 * Exception occurs when one of the user tries to move instead of the opponent.
 */
public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException() {
    }

    public IllegalMoveException(String message) {
        super(message);
    }

    public IllegalMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalMoveException(Throwable cause) {
        super(cause);
    }

    public IllegalMoveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}