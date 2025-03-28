package org.cm.domain.file;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UploadedFileStatus implements PersistenceEnum<String> {
    // @formatter:off
    UPLOADING             ("uploading"),
    COMPLETE_IN_PROGRESS  ("complete_in_progress"),
    COMPLETED             ("completed"),
    ;
    // @formatter:on

    private final String value;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<UploadedFileStatus, String> {
        public Converter() {
            super(UploadedFileStatus.class);
        }
    }
}
