package org.cm.domain.task;

import jakarta.persistence.AttributeConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TaskType {
    // @formatter:off
    VIDEO ("Video"),
    // @formatter:on
    ;

    public final String value;

    public static TaskType of(String value) {
        for (TaskType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TaskType: " + value);
    }

    @jakarta.persistence.Converter
    public static class Converter implements AttributeConverter<TaskType, String> {
        @Override
        public String convertToDatabaseColumn(TaskType taskType) {
            return taskType.value;
        }

        @Override
        public TaskType convertToEntityAttribute(String s) {
            return TaskType.of(s);
        }
    }
}
