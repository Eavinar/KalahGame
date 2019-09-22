package com.example.kalah.service;

import com.example.kalah.engine.GamePlay;
import com.example.kalah.entity.GameBoard;
import com.example.kalah.entity.GameStatus;
import com.example.kalah.entity.User;
import com.example.kalah.entity.UserStep;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;
import com.example.kalah.exceptions.IllegalMoveException;
import com.example.kalah.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Responsible for the business logic of the game.
 */
@Service
public class DefaultGameService implements GameService {
    private final GameRepository gameRepository;
    private final GameBoard gameBoard;
    private final GamePlay gamePlay;

    public DefaultGameService(GameRepository gameRepository, GameBoard gameBoard, GamePlay gamePlay) {
        this.gameRepository = gameRepository;
        this.gameBoard = gameBoard;
        this.gamePlay = gamePlay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUser(final User user) {
        gameRepository.addUser(user);
        updateGameStatus();
    }

    @Override
    public GameStatus removeUser(final User user) {
        gameRepository.removeUser(user);
        if (activeUsersCount() < 2) {
            setGameStatus(GameStatus.DISCONNECTED);
            resetBoard();
        }
        return getGameStatus();
    }

    @Override
    public GameStatus getGameStatus() {
        return gameBoard.getGameStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameStatus(final GameStatus disconnected) {
        gameBoard.setGameStatus(disconnected);
        resetBoard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateGameStatus() {
        switch (activeUsersCount()) {
            case 0:
                gameBoard.setGameStatus(GameStatus.NOT_STARTED);
                break;
            case 1:
                gameBoard.setGameStatus(GameStatus.WAITING_FOR_ANOTHER_USER);
                break;
            case 2:
                gameBoard.setGameStatus(GameStatus.STARTED);
                break;
            default:
                throw new ConnectedUserOutOfAllowanceException("Users count can't be more than 2");
        }
    }

    @Override
    public void startGame() {
        gamePlay.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameBoard move(final UserStep userStep) {
        Optional<User> player = gameRepository.getUsers().stream()
                .filter(user -> user.getName().equals(userStep.getUser().getName())).findAny();
        if (player.isPresent()) {
            return gamePlay.move(player.get(), userStep.getStepId());
        } else {
            throw new IllegalMoveException("User does not exist");
        }
    }

    @Override
    public int activeUsersCount() {
        return gameRepository.getUsersCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameBoard getGameBoard(){
        return gameBoard;
    }

    /**
     * Resetting game to start from scratch.
     */
    private void resetBoard() {
        gameRepository.removeUsers();
        gameBoard.resetBoard();
    }
}
