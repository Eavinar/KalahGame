package com.example.kalah.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class is responsible for the mapping user moves.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStep {
    private String user;
    private String stepId;
}
