package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Token implements Serializable {
    private final long id;
    private final String username, password;
}
