package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class User implements Serializable {
    private final long id;
    private final String username;
    private final String password;
    private final String salt;
    private final Role role;
}
