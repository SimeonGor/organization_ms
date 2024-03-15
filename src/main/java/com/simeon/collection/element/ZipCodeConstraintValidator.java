package com.simeon.collection.element;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Deprecated
public class ZipCodeConstraintValidator implements ConstraintValidator<ZipCodeConstraint, String> {
    ZipCodeConstraint zipCodeConstraint;
    @Override
    public boolean isValid(String zipCode, ConstraintValidatorContext constraintValidatorContext) {
        return zipCode == null || zipCode.length() >= zipCodeConstraint.min();
    }

    @Override
    public void initialize(ZipCodeConstraint constraintAnnotation) {
        zipCodeConstraint = constraintAnnotation;
    }
}
