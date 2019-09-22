package com.example.kalah;

import com.example.kalah.engine.GamePlay;
import com.example.kalah.entity.GameBoard;
import com.example.kalah.entity.GameStatus;
import com.example.kalah.entity.User;
import com.example.kalah.entity.UserStep;
import com.example.kalah.repository.GameRepository;
import com.example.kalah.service.DefaultGameService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.VerificationCollector;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {
    @Rule
    public VerificationCollector verificationCollector = MockitoJUnit.collector();

    @InjectMocks
    DefaultGameService gameService;

    @Mock
    GameRepository gameRepository;

    @Mock
    GamePlay gamePlay;

    @Mock
    GameBoard gameBoard;

    private User user;
    private UserStep userStep;

    @Before
    public void setUp() {
        user = new User("user1");
        userStep = new UserStep(user, "1");
    }

    @Test
    public void addUser() {
        gameService.addUser(user);
        verify(gameRepository, times(1)).addUser(user);
    }

    @Test
    public void removeUserTest() {
        gameService.removeUser(user);
        verify(gameRepository, times(1)).removeUser(user);
    }

    @Test
    public void gameStatusTest() {
        when(gameRepository.getUsersCount()).thenReturn(1);
        gameService.updateGameStatus();
        verify(gameBoard, times(1)).setGameStatus(GameStatus.WAITING_FOR_ANOTHER_USER);

        when(gameRepository.getUsersCount()).thenReturn(2);
        gameService.updateGameStatus();
        verify(gameBoard, times(1)).setGameStatus(GameStatus.STARTED);
    }

    @Test
    public void startGameTest() {
        gameService.startGame();
        verify(gamePlay, times(1)).start();
    }

    @Test
    public void moveTest() {
        when(gameRepository.getUsers()).thenReturn(Arrays.asList(user, user));
        gameService.move(userStep);
        verify(gamePlay, times(1)).move(userStep.getUser(), userStep.getStepId());
    }
}
