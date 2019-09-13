package com.example.kalah.repository;

import com.example.kalah.entity.User;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

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
        if(getUsersCount() > 1){
            throw new ConnectedUserOutOfAllowanceException("Users count more than 2");
        }
        userList.add(user);
    }

    @Override
    public int getUsersCount(){
        return userList.size();
    }

    @Override
    public User getFirstUser(){
        return userList.get(0);
    }

    @Override
    public List<User> getUsers(){
        return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User switchUser(final User currentUser){
        // should add check for Optional.
        return userList.stream().filter(user -> user != currentUser).findAny().get();
    }

    @Override
    public User getSecondUser() {
        return userList.get(1);
    }

    @Override
    public void removeUsers(){
        userList.clear();
    }

    @Override
    public void removeUser(String player) {
        userList.removeIf(user -> user.getName().equals(player));
    }
}
