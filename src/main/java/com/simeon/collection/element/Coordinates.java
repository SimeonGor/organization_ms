package com.simeon.collection.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;

/**
 * Class for organization coordinates
 * @see lombok.Getter
 * @see lombok.Setter
 */
@Getter
@Setter
@ToString
public class Coordinates {
    @Max(value=199, message="Maximum field value: 199")
    private int x;
    private long y;
}
