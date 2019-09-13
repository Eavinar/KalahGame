package com.example.kalah.service;

import com.example.kalah.entity.GameBoard;
import com.example.kalah.entity.GameStatus;
import com.example.kalah.entity.User;
import com.example.kalah.entity.UserStep;

/**
 * Responsible for the business logic of the game.
 */
public interface GameService {
    /**
     * Method adds user into the repository.
     *
     * @param user which is going to be added.
     */
    void addUser(User user);

    GameStatus removeUser(String userName);

    GameStatus getGameStatus();

    /**
     * If user disconnects changing game status and resetting the board.
     *
     * @param disconnected status of the game.
     */
    void setGameStatus(GameStatus disconnected);

    /**
     * Base onf connected users changes game status, respectively.
     */
    void updateGameStatus();

    void startGame();

    /**
     * Method makes move for the user and updates board.
     *
     * @param userStep hold user and move information
     *
     * @return board condition after making the move
     */
    GameBoard move(UserStep userStep);

    int activeUsersCount();
}
