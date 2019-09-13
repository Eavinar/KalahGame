package com.example.kalah;

import com.example.kalah.entity.User;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;
import com.example.kalah.repository.GameRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRepositoryTest {
    @Autowired
    GameRepository gameRepository;

    private User user1;
    private User user2;

    @Before
    public void setUp() {
        user1 = new User("user1");
        user2 = new User("user2");
        gameRepository.removeUsers();
    }

    @Test
    public void addUserTest() {
        assertThat(gameRepository.getUsersCount()).isEqualTo(0);
        gameRepository.addUser(user1);
        assertThat(gameRepository.getUsersCount()).isEqualTo(1);
    }

    @Test
    public void removeUserTest() {
        gameRepository.addUser(user1);
        gameRepository.removeUser(user1.getName());
        assertThat(gameRepository.getUsersCount()).isEqualTo(0);
    }

    @Test(expected = ConnectedUserOutOfAllowanceException.class)
    public void updateGameStatusWhenUserCountExceedsLimitTest() {
        gameRepository.addUser(user1);
        gameRepository.addUser(user2);
        gameRepository.addUser(new User());
    }

    @Test
    public void getFirstUserTest() {
        gameRepository.addUser(user1);
        gameRepository.addUser(user2);
        assertThat(gameRepository.getFirstUser()).isEqualTo(user1);
    }

    @Test
    public void getSecondUserTest() {
        gameRepository.addUser(user1);
        gameRepository.addUser(user2);
        assertThat(gameRepository.getSecondUser()).isEqualTo(user2);
    }

    @Test
    public void switchUserTest() {
        gameRepository.addUser(user1);
        gameRepository.addUser(user2);
        assertThat(gameRepository.switchUser(user1)).isEqualTo(user2);
        assertThat(gameRepository.switchUser(user2)).isEqualTo(user1);
    }
}
