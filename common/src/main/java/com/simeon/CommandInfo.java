package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class CommandInfo {
    protected String name, description;
    protected HashMap<String, Class<? extends Serializable>> parameters;

}
