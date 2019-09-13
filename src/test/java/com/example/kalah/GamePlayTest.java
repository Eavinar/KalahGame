package com.example.kalah;

import com.example.kalah.entity.GameBoard;
import com.example.kalah.entity.GameStatus;
import com.example.kalah.entity.User;
import com.example.kalah.entity.UserStep;
import com.example.kalah.repository.DefaultGameRepository;
import com.example.kalah.engine.GamePlay;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GamePlayTest {
    @Autowired
    GamePlay gamePlay;

    @MockBean
    DefaultGameRepository gameRepository;

    private UserStep user1Step;
    private UserStep user2Step;

    @Before
    public void setUp() {
        User user1 = new User("user1");
        User user2 = new User("user2");
        List<User> userList = Arrays.asList(user1, user2);
        user1Step = new UserStep(user1, "5");
        user2Step = new UserStep(user2, "7");
        when(gameRepository.getUsers()).thenReturn(userList);
        when(gameRepository.getFirstUser()).thenReturn(user1);
        when(gameRepository.getSecondUser()).thenReturn(user2);
        when(gameRepository.switchUser(user1)).thenReturn(user2);
    }

    @Test
    public void startGameTest(){
        gamePlay.start();
        User currentUser = gamePlay.getTurn();
        assertThat(currentUser).isEqualTo(gameRepository.getFirstUser());
    }

    @Test
    public void moveTest(){
        gamePlay.start();
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());
        assertThat(gamePlay.getGameBoard().getGameStatus()).isEqualTo(GameStatus.GOING_ON);
    }

    @Test
    public void gameResultTest(){
        gamePlay.start();
        GameBoard gameBoard = gamePlay.getGameBoard();
        gameBoard.getPits()
                .forEach(pit -> pit.setStonesCount(0));

        gameBoard.getStores().get(0).setStonesCount(20);
        gameBoard.getStores().get(1).setStonesCount(10);
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());
        assertThat(gamePlay.getGameBoard().getGameStatus()).isEqualTo(GameStatus.USER1_WINS);

        gameBoard.getStores().get(0).setStonesCount(10);
        gameBoard.getStores().get(1).setStonesCount(20);
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());
        assertThat(gamePlay.getGameBoard().getGameStatus()).isEqualTo(GameStatus.USER2_WINS);

        gameBoard.getStores().get(0).setStonesCount(10);
        gameBoard.getStores().get(1).setStonesCount(10);
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());
        assertThat(gamePlay.getGameBoard().getGameStatus()).isEqualTo(GameStatus.DRAW);
    }


    @Test
    public void checkPitAndStoreBehaviorForUser1Test(){
        gamePlay.start();
        GameBoard gameBoard = gamePlay.getGameBoard();
        gameBoard.getPits().get(4).setStonesCount(20);
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());

        assertThat(gameBoard.getPits().get(0).getStonesCount()).isEqualTo(7);
        assertThat(gameBoard.getPits().get(1).getStonesCount()).isEqualTo(7);
        assertThat(gameBoard.getPits().get(2).getStonesCount()).isEqualTo(7);
        assertThat(gameBoard.getPits().get(3).getStonesCount()).isEqualTo(7);
        assertThat(gameBoard.getPits().get(4).getStonesCount()).isEqualTo(1);
        assertThat(gameBoard.getPits().get(5).getStonesCount()).isEqualTo(8);

        assertThat(gameBoard.getPits().get(6).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(7).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(8).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(9).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(10).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(11).getStonesCount()).isEqualTo(7);

        assertThat(gameBoard.getStores().get(0).getStonesCount()).isEqualTo(2);
        assertThat(gameBoard.getStores().get(1).getStonesCount()).isEqualTo(0);
    }

    @Test
    public void checkPitAndStoreBehaviorForUser2Test(){
        gamePlay.start();
        GameBoard gameBoard = gamePlay.getGameBoard();
        gameBoard.getPits().get(6).setStonesCount(20);
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());
        gamePlay.move(user2Step.getUser(), user2Step.getStepId());

        assertThat(gameBoard.getPits().get(0).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(1).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(2).getStonesCount()).isEqualTo(7);
        assertThat(gameBoard.getPits().get(3).getStonesCount()).isEqualTo(7);
        assertThat(gameBoard.getPits().get(4).getStonesCount()).isEqualTo(1);
        assertThat(gameBoard.getPits().get(5).getStonesCount()).isEqualTo(8);

        assertThat(gameBoard.getPits().get(6).getStonesCount()).isEqualTo(1);
        assertThat(gameBoard.getPits().get(7).getStonesCount()).isEqualTo(9);
        assertThat(gameBoard.getPits().get(8).getStonesCount()).isEqualTo(9);
        assertThat(gameBoard.getPits().get(9).getStonesCount()).isEqualTo(9);
        assertThat(gameBoard.getPits().get(10).getStonesCount()).isEqualTo(8);
        assertThat(gameBoard.getPits().get(11).getStonesCount()).isEqualTo(8);

        assertThat(gameBoard.getStores().get(0).getStonesCount()).isEqualTo(1);
        assertThat(gameBoard.getStores().get(1).getStonesCount()).isEqualTo(2);
    }

    @Test
    public void eatOppositePitForUser1Test(){
        gamePlay.start();
        GameBoard gameBoard = gamePlay.getGameBoard();
        gameBoard.getPits().get(4).setStonesCount(1);
        gameBoard.getPits().get(5).setStonesCount(0);
        gameBoard.getPits().get(6).setStonesCount(10);
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());
        assertThat(gameBoard.getStores().get(0).getStonesCount()).isEqualTo(11);
    }

    @Test
    public void eatOppositePitForUser2Test(){
        gamePlay.start();
        GameBoard gameBoard = gamePlay.getGameBoard();
        gamePlay.move(user1Step.getUser(), user1Step.getStepId());

        gameBoard.getPits().get(6).setStonesCount(1);
        gameBoard.getPits().get(7).setStonesCount(0);
        gameBoard.getPits().get(4).setStonesCount(10);
        gamePlay.move(user2Step.getUser(), user2Step.getStepId());
        assertThat(gameBoard.getStores().get(1).getStonesCount()).isEqualTo(11);
    }
}
