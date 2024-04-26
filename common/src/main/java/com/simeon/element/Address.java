package com.simeon.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Organization's address
 */

@Getter
@Setter
@ToString
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    /**
     * The string must be at least 0 characters long or empty.
     */
    @Size(min=9, message="The string must be at least {min} characters long or empty.")
    private String zipCode; //Длина строки должна быть не меньше 9, Поле может быть null
}
