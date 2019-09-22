package com.example.kalah.exceptions;

/**
 * Exception occurs when more than 2 players tries to connect the game.
 */
public class ConnectedUserOutOfAllowanceException extends RuntimeException {
    public ConnectedUserOutOfAllowanceException(String message) {
        super(message);
    }
}
