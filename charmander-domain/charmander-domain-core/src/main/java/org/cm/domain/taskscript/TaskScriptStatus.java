package org.cm.domain.taskscript;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@RequiredArgsConstructor
public enum TaskScriptStatus implements PersistenceEnum<String> {
    // @formatter:off
    PENDING     ("Pending"   , "대기중"),
    IN_PROGRESS ("InProgress", "처리중"),
    SUCCESS     ("Success"   , "성공"  ),
    FAILED      ("Failed"    , "실패"  ),
    CANCELED    ("Canceled"  , "취소됨"),
    ;

    private final String value;
    private final String description;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<TaskScriptStatus, String> {
        public Converter() {
            super(TaskScriptStatus.class);
        }
    }
}
