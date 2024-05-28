package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
@Setter
@AllArgsConstructor
public class CommandInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private String name, description;
    private LinkedHashMap<String, Class<? extends Serializable>> parameters;
}
