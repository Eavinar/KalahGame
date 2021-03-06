package com.example.kalah.engine;

import com.example.kalah.entity.GameBoard;
import com.example.kalah.entity.User;
import com.example.kalah.exceptions.IllegalMoveException;

public interface GamePlay {
    User getTurn();

    GameBoard getGameBoard();

    /**
     * Method starts the game. Responsible for the board initializing,
     * assigning users.
     */
    void start();

    /**
     * Method is responsible for the main logic of the game.
     *
     * @param user holds user name and move information.
     * @param stepId holds move information.
     *
     * @return board information.
     *
     * @throws IllegalMoveException if user tries to make a move instead of opponent
     */
    GameBoard move(User user, String stepId);
}
