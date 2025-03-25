package org.cm.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;

@Getter
@RequiredArgsConstructor
public enum BaseExceptionCode implements ExceptionCode{
    NOT_FOUND_ELEMENT("A01", "원소를 찾을 수 없습니다.", LogLevel.WARN);

    private final String code;
    private final String message;
    private final LogLevel level;

    @Override
    public String getPrefix() {
        return "CM";
    }


}
