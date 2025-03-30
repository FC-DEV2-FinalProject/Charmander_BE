package org.cm.domain.file;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UploadedFileType implements PersistenceEnum<String> {
    // @formatter:off
    USER_UPLOAD   ("user:upload"),
    SYSTEM        ("system"),
    ;
    // @formatter:on

    private final String value;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<UploadedFileType, String> {
        public Converter() {
            super(UploadedFileType.class);
        }
    }
}
