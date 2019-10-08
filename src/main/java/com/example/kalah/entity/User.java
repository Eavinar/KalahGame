package com.example.kalah.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Class holds User details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return  true;
        if(!(obj instanceof User)){
            return false;
        }
        User user = (User) obj;
        return name.equals(user.getName());
    }
}
