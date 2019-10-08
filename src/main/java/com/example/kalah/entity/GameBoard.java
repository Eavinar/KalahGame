package com.example.kalah.entity;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all game board related information.
 * Also in charge for the nitialization of the board and reset it.
 */
@Getter
@Component
public class GameBoard {
    private List<Pit> pits = new ArrayList<>(12);
    private List<Store> stores = new ArrayList<>(2);
    private GameStatus gameStatus;
    private String message;

    public void setGameStatus(final GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.message = gameStatus.getMessage();
    }

    /**
     * Method initialize board with necessary requirements.
     * Assigns stones to the pits, changes game status.
     *
     * @param user1 first User of the game.
     * @param user2 second User of the game.
     */
    public void initBoard(final User user1, final User user2) {
        for (int i = 0; i < 6; i++) {
            pits.add( new Pit(user1, 6));
        }
        for (int i = 0; i < 6; i++) {
            pits.add(new Pit(user2, 6));
        }
        stores.add(new Store(user1, 0));
        stores.add(new Store(user2, 0));
        setGameStatus(GameStatus.GOING_ON);
    }

    /**
     * Method resets board, empties pit and store.
     */
    public void resetBoard() {
        pits = new ArrayList<>(12);
        stores = new ArrayList<>(2);
    }
}
