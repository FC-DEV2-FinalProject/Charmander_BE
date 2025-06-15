package org.cm.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoubleRangeValidator implements ConstraintValidator<DoubleRange, Double> {
    private Double min;
    private Double max;

    private boolean allowEmpty;

    @Override
    public void initialize(DoubleRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowEmpty;
        }

        return value >= min && value <= max;
    }
}
