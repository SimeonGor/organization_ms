package com.simeon.collection.element;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=AnnualTurnoverValidator.class)
public @interface AnnualTurnoverConstraint {
    String message() default "The string must be grater than {min}.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    double min() default 0;
}
