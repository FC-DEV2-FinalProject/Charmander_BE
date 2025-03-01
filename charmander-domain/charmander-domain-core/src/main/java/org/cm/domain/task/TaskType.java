package org.cm.domain.task;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TaskType implements PersistenceEnum<String> {
    // @formatter:off
    VIDEO ("Video"),
    ;
    // @formatter:on

    private final String value;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<TaskType, String> {
        public Converter() {
            super(TaskType.class);
        }
    }
}
