package org.cm.common.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.cm.common.exception.BaseException;
import org.cm.common.exception.BaseExceptionCode;

import java.util.Arrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FileType {
    // @formatter:off
    PNG  ("image/png"),
    JPG  ("image/jpg"),
    SVG  ("image/svg+xml"),
    JPEG ("image/jpeg"),
    WAV  ("audio/wav")
    ;
    // @formatter:on

    public final String mimeType;

    public static FileType from(String extension) {
        String finalExtension = extension.toUpperCase();
        return Arrays.stream(values())
                .filter(v -> v.name().equals(finalExtension))
                .findFirst()
                .orElseThrow(() -> new BaseException(BaseExceptionCode.NOT_FOUND_ELEMENT));
    }
}
