package org.cm.common.domain;

import java.util.Arrays;
import org.cm.common.exception.BaseException;
import org.cm.common.exception.BaseExceptionCode;

public enum FileType {
    PNG,
    JPG,
    SVG,
    JPEG
    ;

    public static FileType from(String extension) {
        String finalExtension = extension.toUpperCase();
        return Arrays.stream(values())
                .filter(v -> v.name().equals(finalExtension))
                .findFirst()
                .orElseThrow(() -> new BaseException(BaseExceptionCode.NOT_FOUND_ELEMENT));
    }
}
