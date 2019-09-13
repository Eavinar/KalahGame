package com.example.kalah.engine;

import com.example.kalah.entity.*;
import com.example.kalah.exceptions.IllegalMoveException;
import com.example.kalah.repository.GameRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DefaultGamePlay implements GamePlay {

    private final GameRepository gameRepository;
    private final GameBoard gameBoard;
    // indicates which users turn, now.
    private User turn;

    public DefaultGamePlay(GameRepository gameRepository, GameBoard gameBoard) {
        this.gameRepository = gameRepository;
        this.gameBoard = gameBoard;
    }

    @Override
    public User getTurn() {
        return turn;
    }

    private void setTurn(User turn) {
        this.turn = turn;
    }

    @Override
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        List<User> users = gameRepository.getUsers();
        gameBoard.initBoard(users.get(0), users.get(1));
        setTurn(gameRepository.getFirstUser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameBoard move(final UserStep userStep) {
        // checking whether it is good to go on
        if (gameBoard.getGameStatus() == GameStatus.GOING_ON) {
            User firstUser = gameRepository.getFirstUser();
            // counting in web page starts from 1, so -1 here.
            int selectedPitIndex = Integer.parseInt(userStep.getStepId()) - 1;
            Pit selectedPit = gameBoard.getPits().get(selectedPitIndex);
            int stonesInPit = selectedPit.getStonesCount();
            // based on user turn defining virtual index of the store.
            int storeIndex = (getTurn() == firstUser ? 6 : 0);

            if (!userStep.getUser().equals(getTurn().getName())) {
                throw new IllegalMoveException("It is not " + userStep.getUser() + " turn");
            }

            // if owner is who's turn now.
            if (selectedPit.getUser() == getTurn()) {
                selectedPit.setStonesCount(0);

                //iterating based on how many stones was in selected pit
                for (int i = 1; i <= stonesInPit; i++) {
                    // if stones count is more than pit count we reassign next index number.
                    // basically cycling.
                    int nextPitIndex = (selectedPitIndex + i) % 12;

                    // if pit index is the same with virtual store index
                    if (nextPitIndex == storeIndex) {
                        addStoneToStore(getTurn());
                        // if there is no more stones, breaking loop, so user can continue.
                        if (i > --stonesInPit) {
                            break;
                        }
                    }

                    addStoneToPit(nextPitIndex);

                    // if it is the last stone.
                    if (i == stonesInPit) {
                        Pit lastPit = gameBoard.getPits().get(nextPitIndex);
                        int stonesInLastPit = lastPit.getStonesCount();

                        // if the last pit belongs to the current users'.
                        // And count of the left stones is 1 instead of 0.
                        // Because of we are checking after the adding last stone to the pit.
                        if (stonesInLastPit == 1 && lastPit.getUser() == getTurn()) {
                            // getting store details for the user.
                            // should add check for Optional.
                            Store userStore = gameBoard.getStores().stream()
                                    .filter(store -> store.getUser() == getTurn()).findAny().get();

                            // Updating user's store with adding oposite pit stones
                            int stonesInStore = userStore.getStonesCount();
                            Pit oppositePit = gameBoard.getPits().get(11 - nextPitIndex);
                            int oppsiteStones = oppositePit.getStonesCount();
                            int totalStonesInStore = stonesInStore + oppsiteStones + stonesInLastPit;
                            userStore.setStonesCount(totalStonesInStore);
                            // Clear the pits.
                            lastPit.setStonesCount(0);
                            oppositePit.setStonesCount(0);
                        }
                        // Change user turn.
                        setTurn(gameRepository.switchUser(getTurn()));
                    }
                }
            }
        }
        // check game status, whether good to go.
        checkGameStatus();
        return gameBoard;
    }

    /**
     * Method checks game status. Base on easy calculations defines the next step.
     */
    private void checkGameStatus() {
        boolean isGameOverByFirstUser = isGameOverByUser(gameRepository.getFirstUser());
        boolean isGameOverBySecondUser = isGameOverByUser(gameRepository.getSecondUser());
        boolean isGameOver = isGameOverByFirstUser || isGameOverBySecondUser;
        int firstUserStoreStones = 0;
        int secondUserStoreStones = 0;

        // logic to calculate users' stones.
        if (isGameOverByFirstUser) {
            firstUserStoreStones = gameBoard.getStores().get(0).getStonesCount();
            secondUserStoreStones = calculateUserStones(gameRepository.getSecondUser(), 1);
        } else if (isGameOverBySecondUser) {
            firstUserStoreStones = calculateUserStones(gameRepository.getFirstUser(), 0);
            secondUserStoreStones = gameBoard.getStores().get(1).getStonesCount();
        }

        // The moment of the truth. Finding out winner.
        if (isGameOver) {
            if (firstUserStoreStones > secondUserStoreStones) {
                gameBoard.setGameStatus(GameStatus.USER1_WINS);
            } else if (firstUserStoreStones < secondUserStoreStones) {
                gameBoard.setGameStatus(GameStatus.USER2_WINS);
            } else {
                gameBoard.setGameStatus(GameStatus.DRAW);
            }
        }
    }

    /**
     * Method adds stones left in the user's pit into the store.
     *
     * @param user for whom calculation will be.
     * @param userIndex of store for the user.
     *
     * @return total of the stones user has
     */
    private int calculateUserStones(final User user, final int userIndex) {
        int userStoreStones = gameBoard.getStores().get(userIndex).getStonesCount();
        userStoreStones = userStoreStones + getStonesLeftInPits(user);
        gameBoard.getStores().get(userIndex).setStonesCount(userStoreStones);
        emptyPits();
        return userStoreStones;
    }

    private void emptyPits() {
        gameBoard.getPits()
                .forEach(pit -> pit.setStonesCount(0));
    }

    /**
     * Method validates if the user has stones in his pits.
     * If yes returns {@code true}, otherwiae {@code false}.
     *
     * @param user is owner of the pits.
     *
     * @return whether game over for the user.
     */
    private boolean isGameOverByUser(final User user) {
        return gameBoard.getPits().stream()
                .filter(pit -> pit.getUser() == user)
                .allMatch(pit -> pit.getStonesCount() == 0);
    }

    private int getStonesLeftInPits(final User user) {
        int stonesLeftInPits;
        stonesLeftInPits = gameBoard.getPits().stream()
                .filter(pit -> pit.getUser() == user)
                .mapToInt(Pit::getStonesCount).sum();
        return stonesLeftInPits;
    }

    private void addStoneToPit(final int nextPitIndex) {
        Pit nextPit = gameBoard.getPits().get(nextPitIndex);
        int stoneCount = nextPit.getStonesCount();
        nextPit.setStonesCount(stoneCount + 1);
    }

    private void addStoneToStore(final User user) {
        // should add check for Optional.
        Store userStore = gameBoard.getStores().stream()
                .filter(store -> store.getUser() == user).findAny().get();
        int stoneCount = userStore.getStonesCount();
        userStore.setStonesCount(stoneCount + 1);
    }
}
