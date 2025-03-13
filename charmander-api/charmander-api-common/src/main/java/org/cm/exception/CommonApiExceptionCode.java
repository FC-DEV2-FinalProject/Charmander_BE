package org.cm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cm.common.exception.ExceptionCode;
import org.springframework.boot.logging.LogLevel;

@Getter
@RequiredArgsConstructor
public enum CommonApiExceptionCode implements ExceptionCode {
    // @formatter:off
    EMAIL_SENT_FAIL ("E001", "이메일 발송에 실패하였습니다."),
    ;
    // @formatter:on

    // Core domain
    private static final String PREFIX = "COAPI";

    private final String code;
    private final String message;
    private final LogLevel level;

    // 예외 코드의 기본 설정은 INFO
    CommonApiExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.level = LogLevel.INFO;
    }

    @Override
    public String getPrefix() {
        return "COAPI";
    }
}
