package org.cm.domain.task;

import jakarta.persistence.AttributeConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TaskStatus {
    // @formatter:off
    PENDING     ("Pending"   , "대기중"),
    IN_PROGRESS ("InProgress", "처리중"),
    SUCCESS     ("Success"   , "성공"  ),
    FAILED      ("Failed"    , "실패"  ),
    CANCELED    ("Canceled"  , "취소됨"),
    ;
    // @formatter:on

    public final String value;
    public final String description;

    public static TaskStatus of(String value) {
        for (TaskStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid TaskStatus: " + value);
    }

    @jakarta.persistence.Converter
    public static class Converter implements AttributeConverter<TaskStatus, String> {
        @Override
        public String convertToDatabaseColumn(TaskStatus taskStatus) {
            return taskStatus.value;
        }

        @Override
        public TaskStatus convertToEntityAttribute(String s) {
            return TaskStatus.of(s);
        }
    }
}
