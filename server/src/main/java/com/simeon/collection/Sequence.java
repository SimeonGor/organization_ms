package com.simeon.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Sequence {
    private long i;
    private final long increment;
    private final long maxvalue;

    public long nextValue() {
        i += increment;
        if (i < maxvalue) {
            return i;
        }
        else {
            return 1L;
        }
    }
}
