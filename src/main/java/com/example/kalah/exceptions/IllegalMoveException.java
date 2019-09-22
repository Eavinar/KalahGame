package com.example.kalah.exceptions;

/**
 * Exception occurs when one of the user tries to move instead of the opponent.
 */
public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(String message) {
        super(message);
    }
}
