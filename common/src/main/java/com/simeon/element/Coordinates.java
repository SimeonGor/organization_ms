package com.simeon.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;

/**
 * Class for organization coordinates
 */
@Getter
@Setter
@ToString
public class Coordinates {
    /**
     * Maximum field value: 199
     */
    @Max(value=199, message="Maximum field value: {value}")
    private int x;
    private long y;
}
