package com.example.kalah.entity;

/**
 * Class is responsible for the different statuses faced in the game.
 */
public enum GameStatus {
    USER1_WINS("First user win"),
    USER2_WINS("Second user win"),
    DRAW("Friendship wins"),
    GOING_ON("Game is going on"),
    WAITING_FOR_ANOTHER_USER("Waiting for the another user.."),
    NOT_STARTED("No user connected"),
    STARTED("Game started.."),
    DISCONNECTED("Opponent disconnected. You win");

    private final String message;

    GameStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}