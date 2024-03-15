package com.simeon.collection.element;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ZipCodeConstraintValidator.class)
public @interface ZipCodeConstraint{
    String message() default "The string must be at least {min} characters long or empty.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int min() default 9;
}
