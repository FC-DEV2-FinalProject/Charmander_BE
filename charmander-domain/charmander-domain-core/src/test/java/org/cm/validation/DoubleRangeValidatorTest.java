package org.cm.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[단위 테스트] DoubleRangeValidator")
class DoubleRangeValidatorTest {
    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    @AfterEach
    public void tearDown() {
        this.validator = null;
    }

    @Test
    @DisplayName("min 값보다 작은 값이 들어오면 유효성 검사 실패")
    void validation_fail_on_value_less_than_min() {
        var entity = new Entity(0);
        var violations = this.validator.validate(entity);

        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("min 값과 같은 값이 들어오면 유효성 검사 성공")
    void validation_fail_on_value_equals_min() {
        var entity = new Entity(1);
        var violations = this.validator.validate(entity);

        assertEquals(0, violations.size());
    }


    @Test
    @DisplayName("max 값보다 큰 값이 들어오면 유효성 검사 실패")
    void validation_fail_on_value_grater_than_max() {
        var entity = new Entity(20);
        var violations = this.validator.validate(entity);

        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("max 값과 같은 값이 들어오면 유효성 검사 성공")
    void validation_fail_on_value_equals_max() {
        var entity = new Entity(10);
        var violations = this.validator.validate(entity);

        assertEquals(0, violations.size());
    }

    static class Entity {
        @DoubleRange(min = 1, max = 10)
        double value;

        public Entity(double value) {
            this.value = value;
        }
    }
}
