package com.example.kalah.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class holds store information for Kalah Game
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private User user;
    private int stonesCount;
}
