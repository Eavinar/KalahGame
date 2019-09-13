package com.example.kalah.controller;

import com.example.kalah.entity.*;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;
import com.example.kalah.exceptions.IllegalMoveException;
import com.example.kalah.service.GameService;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Class is responsible for the handling socket requests and routing them.
 */
@Controller
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Method connects user to the game.
     *
     * @param user mapped information about user.
     *
     * @return the status after the connection.
     */
    @MessageMapping("/game")
    @SendTo("/topic/connection")
    public String connect(final User user) {
        gameService.addUser(user);
        GameStatus gameStatus = gameService.getGameStatus();
        if (gameStatus == GameStatus.STARTED) {
            gameService.startGame();
        }
        return gameStatus.getMessage();
    }

    /**
     * Method handles user's move and interacts with back layers.
     *
     * @param userStep holds information about user and user's move.
     *
     * @return updated game board.
     */
    @MessageMapping("/updateBoard")
    @SendTo("/topic/move")
    public GameBoard userMove(final UserStep userStep) {
        return gameService.move(userStep);
    }

    /**
     * Method disconnects user.
     *
     * @param user which should be disconnected.
     *
     * @return message base on disconnect action.
     */
    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnect")
    public String disconnect(final String user) {
        gameService.removeUser(user);
        System.out.println(gameService.activeUsersCount());
        if(gameService.activeUsersCount() < 2){
            gameService.setGameStatus(GameStatus.DISCONNECTED);
        }
        return gameService.getGameStatus().getMessage();
    }

    /**
     * Method handles exception for the wrong user's move attempt.
     * And send message accordingly.
     *
     * @param userStep hold user name and step.
     *
     * @return message should be alerted.
     */
    @MessageExceptionHandler(IllegalMoveException.class)
    @SendTo("/topic/messages")
    public Alert illegalMove(final UserStep userStep) {
        Alert alert = new Alert();
        alert.setUser(userStep.getUser());
        alert.setMessage("It is not your turn");
        alert.setStatus("OK");
        return alert;
    }

    /**
     * Method handles exception for the connections count
     * which is more than thresold (currently 2 users).
     *
     * @param user tried to connect
     *
     * @return message should be alerted.
     */
    @MessageExceptionHandler(ConnectedUserOutOfAllowanceException.class)
    @SendTo("/topic/messages")
    public Alert boardIsBusy(final User user) {
        Alert alert = new Alert();
        alert.setUser(user.getName());
        alert.setMessage("Board is busy, please wait...");
        alert.setStatus("FAIL");
        return alert;
    }
}
