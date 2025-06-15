package org.cm.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IntRangeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntRange {
    int min();
    int max();

    boolean allowEmpty() default false;

    String message() default "Value is out of range";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
