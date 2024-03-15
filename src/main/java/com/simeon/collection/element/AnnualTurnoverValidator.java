package com.simeon.collection.element;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
