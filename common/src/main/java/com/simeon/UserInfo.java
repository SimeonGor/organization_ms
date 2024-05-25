package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserInfo implements Serializable {
    private final long id;
    private final String username;
    private final Role role;
}
