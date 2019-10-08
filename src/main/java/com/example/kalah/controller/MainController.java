package com.example.kalah.controller;

import com.example.kalah.entity.GameBoard;
import com.example.kalah.entity.GameStatus;
import com.example.kalah.entity.User;
import com.example.kalah.entity.UserStep;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This interface manages game behavior
 */
public interface MainController {
    /**
     * Method connects user to the game.
     *
     * @param user mapped information about user.
     *
     * @return the status after the connection.
     */
    ResponseEntity<String> connect(@RequestBody User user);

    /**
     * Method handles user's move and interacts with back layers.
     *
     * @param userStep holds information about user and user's move.
     *
     * @return updated game board.
     */
    ResponseEntity<GameBoard> userMove(@RequestBody UserStep userStep);

    /**
     * Method disconnects user from the game.
     *
     * @return current board condition.
     */
    ResponseEntity<GameBoard> gameStatus();

    /**
     * Method disconnects user from the game.
     *
     * @param user which should be disconnected.
     *
     * @return message base on disconnect action.
     */
    ResponseEntity<GameStatus> disconnect(@RequestBody User user);
}
