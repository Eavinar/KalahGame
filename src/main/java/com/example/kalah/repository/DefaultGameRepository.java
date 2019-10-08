package com.example.kalah.repository;

import com.example.kalah.entity.User;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;
import com.example.kalah.exceptions.IllegalMoveException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository is responsible for the keeping and manipulating information about user.
 */
@Repository
public class DefaultGameRepository implements GameRepository {
    private static final List<User> userList = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUser(final User user) {
        if(userList.contains(user)){
            throw new ConnectedUserOutOfAllowanceException("User with this name exists");
        }
        if (getUsersCount() > 1) {
            throw new ConnectedUserOutOfAllowanceException("Users count more than 2");
        }
        userList.add(user);
    }

    @Override
    public int getUsersCount() {
        return userList.size();
    }

    @Override
    public User getFirstUser() {
        return userList.get(0);
    }

    @Override
    public List<User> getUsers() {
        return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User switchUser(final User currentUser) {
        Optional<User> player = userList.stream().filter(user -> user != currentUser).findAny();
        if (player.isPresent()) {
            return player.get();
        } else {
            throw new IllegalMoveException("User does not exist");
        }
    }

    @Override
    public User getSecondUser() {
        return userList.get(1);
    }

    @Override
    public void removeUsers() {
        userList.clear();
    }

    @Override
    public void removeUser(User player) {
        userList.removeIf(user -> user.equals(player));
    }
}
