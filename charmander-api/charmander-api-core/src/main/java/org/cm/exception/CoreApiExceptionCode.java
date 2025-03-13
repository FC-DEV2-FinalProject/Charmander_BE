package org.cm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cm.common.exception.WebExceptionCode;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum CoreApiExceptionCode implements WebExceptionCode {
    // @formatter:off
    ACCOUNT_EMAIL_ALREADY_IN_USE("A001", "Email is already in use."),
    ACCOUNT_VERIFICATION_CODE_NOT_MATCH("A002", "Verification code does not match."),

    AUTH_INVALID_CREDENTIAL("AU001", "Invalid username or password."),

    MEMBER_NOT_FOUND("M001", "Member not found."),

    PROJECT_NOT_FOUND("P001", "Project not found.", HttpStatus.NOT_FOUND),

    TASK_NOT_FOUND("T001", "Task not found."),

    TEMPLATE_NOT_FOUND("TP001", "Template not found.")
    ;
    // @formatter:on

    // Core domain
    private static final String PREFIX = "CORAPI";

    private final String code;
    private final String message;
    private final LogLevel level;
    private final HttpStatusCode httpStatusCode;

    // 예외 코드의 기본 설정은 INFO
    CoreApiExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.level = LogLevel.INFO;
        this.httpStatusCode = HttpStatus.BAD_REQUEST;
    }

    CoreApiExceptionCode(String code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.level = LogLevel.INFO;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getPrefix() {
        return "CORAPI";
    }

    @Override
    public HttpStatusCode httpStatusCode() {
        return httpStatusCode;
    }
}
