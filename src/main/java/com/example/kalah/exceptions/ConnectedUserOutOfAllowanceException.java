package com.example.kalah.exceptions;

/**
 * Exception occurs when more than 2 players tries to connect the game.
 */
public class ConnectedUserOutOfAllowanceException extends RuntimeException {
    public ConnectedUserOutOfAllowanceException() {
    }

    public ConnectedUserOutOfAllowanceException(String message) {
        super(message);
    }

    public ConnectedUserOutOfAllowanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectedUserOutOfAllowanceException(Throwable cause) {
        super(cause);
    }

    public ConnectedUserOutOfAllowanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
