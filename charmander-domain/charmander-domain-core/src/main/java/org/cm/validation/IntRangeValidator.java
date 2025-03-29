package org.cm.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IntRangeValidator implements ConstraintValidator<IntRange, Integer> {
    private Integer min;
    private Integer max;

    private boolean allowEmpty;

    @Override
    public void initialize(IntRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowEmpty;
        }

        return value >= min && value <= max;
    }
}
