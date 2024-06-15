package com.simeon;

import java.io.Serializable;

public enum ResponseStatus implements Serializable {
    OK,
    ADD,
    UPDATE,
    DELETE,
    ERROR
}
