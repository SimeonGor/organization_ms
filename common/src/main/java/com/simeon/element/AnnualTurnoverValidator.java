package com.simeon.element;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Annual turnover validator
 * @see ConstraintValidator
 */
public class AnnualTurnoverValidator implements ConstraintValidator<AnnualTurnoverConstraint, Double> {
    AnnualTurnoverConstraint annualTurnoverConstraint;
    @Override
    public boolean isValid(Double annualTurnover, ConstraintValidatorContext constraintValidatorContext) {
        return annualTurnover > annualTurnoverConstraint.min();
    }

    @Override
    public void initialize(AnnualTurnoverConstraint annualTurnoverConstraint) {
        this.annualTurnoverConstraint = annualTurnoverConstraint;
    }
}
