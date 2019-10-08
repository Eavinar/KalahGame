package com.example.kalah.controller;

import com.example.kalah.entity.*;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;
import com.example.kalah.exceptions.IllegalMoveException;
import com.example.kalah.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Class is responsible for the handling socket requests and routing them.
 */
@Controller
public class SocketController implements MainController {
    private final GameService gameService;

    public SocketController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * {@inheritDoc}
     */
    @MessageMapping("/game")
    @SendTo("/topic/connection")
    public ResponseEntity<String> connect(final User user) {
        gameService.addUser(user);
        GameStatus gameStatus = gameService.getGameStatus();
        if (gameStatus == GameStatus.STARTED) {
            gameService.startGame();
        }
        return new ResponseEntity<>(gameStatus.getMessage(), HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @MessageMapping("/updateBoard")
    @SendTo("/topic/move")
    public ResponseEntity<GameBoard> userMove(final UserStep userStep) {
        return new ResponseEntity<>(gameService.move(userStep), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GameBoard> gameStatus() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnect")
    public ResponseEntity<GameStatus> disconnect(final User user) {
        gameService.removeUser(user);
        if(gameService.activeUsersCount() < 2){
            gameService.setGameStatus(GameStatus.DISCONNECTED);
        }
        return new ResponseEntity<>(gameService.getGameStatus(), HttpStatus.OK);
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
    public Alert illegalMove(final UserStep userStep, final Exception exception) {
        Alert alert = new Alert();
        alert.setUser(userStep.getUser());
        alert.setMessage(exception.getMessage());
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
    public Alert boardIsBusy(final User user, final Exception exception) {
        Alert alert = new Alert();
        alert.setUser(user);
        alert.setMessage(exception.getMessage());
        alert.setStatus("FAIL");
        return alert;
    }
}
