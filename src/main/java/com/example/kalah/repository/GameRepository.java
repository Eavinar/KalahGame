package com.example.kalah.repository;

import com.example.kalah.entity.User;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;

import java.util.List;

public interface GameRepository {
    /**
     * Method adds user to the repository. Before adding validates if threshold from 2 people exceeded.
     *     *
     * @param user which is going to be added.
     * @exception ConnectedUserOutOfAllowanceException if threshold exceeded.
     */
    void addUser(User user);

    int getUsersCount();

    User getFirstUser();

    List<User> getUsers();

    /**
     * Method switches between the users.
     *
     * @param currentUser based on this user switches to another user
     *
     * @return another user
     */
    User switchUser(User currentUser);

    User getSecondUser();

    void removeUsers();

    void removeUser(String userName);
}
