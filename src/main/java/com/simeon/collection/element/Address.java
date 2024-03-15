package com.simeon.collection.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

/**
 * Class for organization address
 */

@Getter
@Setter
@ToString
public class Address {
    @Size(min=9, message="The string must be at least {min} characters long or empty.")
    private String zipCode; //Длина строки должна быть не меньше 9, Поле может быть null
}
