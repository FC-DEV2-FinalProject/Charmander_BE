package org.cm.domain.task;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TaskStatus implements PersistenceEnum<String> {
    // @formatter:off
    PENDING     ("Pending"   , "대기중"),
    IN_PROGRESS ("InProgress", "처리중"),
    CONVERTING  ("Converting", "변환중"),
    SUCCESS     ("Success"   , "성공"  ),
    FAILED      ("Failed"    , "실패"  ),
    CANCELED    ("Canceled"  , "취소됨"),
    ;
    // @formatter:on

    private final String value;
    private final String description;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<TaskStatus, String> {
        public Converter() {
            super(TaskStatus.class);
        }
    }
}
