package com.simeon.element;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annual turnover constraint annotation
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=AnnualTurnoverValidator.class)
public @interface AnnualTurnoverConstraint {
    String message() default "The value must be grater than {min}.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    double min() default 0;
}
