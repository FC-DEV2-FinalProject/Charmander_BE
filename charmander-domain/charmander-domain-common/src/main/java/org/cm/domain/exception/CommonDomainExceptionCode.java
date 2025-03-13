package org.cm.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cm.common.exception.ExceptionCode;
import org.springframework.boot.logging.LogLevel;

@Getter
@RequiredArgsConstructor
public enum CommonDomainExceptionCode implements ExceptionCode {

    ELEMENT_NOT_FOUND("A01", "enum의 원소와 데이터베이스의 컬럼의 값이 일치하지 않습니다.", LogLevel.ERROR)
    ;

    // TODO: 나중에 다른 식별자로 바꾸는 것도 고민해보면 좋을 듯?
    private static final String PREFIX = "CODOM";

    private final String code;
    private final String message;
    private final LogLevel level;


    @Override
    public String getPrefix() {
        // core domain
        return PREFIX;
    }
}
