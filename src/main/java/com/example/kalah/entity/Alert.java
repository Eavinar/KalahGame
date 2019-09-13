package com.example.kalah.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class is responsible for the sending alert messages.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    private String user;
    private String message;
    private String status;
}
