package com.simeon.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import java.io.Serial;
import java.io.Serializable;

/**
 * Class for organization coordinates
 */
@Getter
@Setter
@ToString
public class Coordinates implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    /**
     * Maximum field value: 199
     */
    @Max(value=199, message="Maximum field value: {value}")
    private int x;
    private long y;
}
