package com.example.kalah.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Class holds Pit information for Kalah Game
 */
@Getter
@Setter
@AllArgsConstructor
public class Pit {
    private User user;
    private int stonesCount;
}
