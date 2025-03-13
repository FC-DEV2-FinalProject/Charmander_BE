package org.cm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cm.common.exception.ExceptionCode;
import org.springframework.boot.logging.LogLevel;

@Getter
@RequiredArgsConstructor
public enum CoreApiExceptionCode implements ExceptionCode {
    // @formatter:off
    ACCOUNT_EMAIL_ALREADY_IN_USE("A001", "Email is already in use."),
    ACCOUNT_VERIFICATION_CODE_NOT_MATCH("A002", "Verification code does not match."),
    ;
    // @formatter:on

    // Core domain
    private static final String PREFIX = "CORAPI";

    private final String code;
    private final String message;
    private final LogLevel level;

    // 예외 코드의 기본 설정은 INFO
    CoreApiExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.level = LogLevel.INFO;
    }

    @Override
    public String getPrefix() {
        return "CORAPI";
    }
}
