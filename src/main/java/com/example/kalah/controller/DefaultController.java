package com.example.kalah.controller;

import com.example.kalah.entity.GameBoard;
import com.example.kalah.entity.GameStatus;
import com.example.kalah.entity.User;
import com.example.kalah.entity.UserStep;
import com.example.kalah.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController implements MainController {
    private final GameService gameService;

    public DefaultController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping("/react/connect")
    public ResponseEntity<String> connect(@RequestBody final User user) {
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
    @Override
    @PostMapping("/react/move")
    public ResponseEntity<GameBoard> userMove(@RequestBody final UserStep userStep) {
        return new ResponseEntity<>(gameService.move(userStep), HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping("/react/getGameStatus")
    public ResponseEntity<GameBoard> gameStatus() {
        return new ResponseEntity<>(gameService.getGameBoard(), HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping("/react/disconnect")
    public ResponseEntity<GameStatus> disconnect(@RequestBody final User user) {
        gameService.removeUser(user);
        if (gameService.activeUsersCount() < 2) {
            gameService.setGameStatus(GameStatus.DISCONNECTED);
        }
        return new ResponseEntity<>(gameService.getGameStatus(), HttpStatus.OK);
    }
}
