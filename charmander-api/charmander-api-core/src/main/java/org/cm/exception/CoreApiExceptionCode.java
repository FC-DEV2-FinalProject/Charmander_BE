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
    UNAUTHORIZED("AU001", "Unauthorized.", LogLevel.WARN, HttpStatus.UNAUTHORIZED),

    ACCOUNT_EMAIL_ALREADY_IN_USE("A001", "Email is already in use."),
    ACCOUNT_VERIFICATION_CODE_NOT_MATCH("A002", "Verification code does not match."),

    REFRESH_TOKEN_VERIFICATION_FAILED("RT001", "Refresh token verification failed.", HttpStatus.UNAUTHORIZED),

    AUTH_INVALID_CREDENTIAL("AU001", "Invalid username or password."),

    MEMBER_NOT_FOUND("M001", "Member not found.", HttpStatus.NOT_FOUND),

    PROJECT_NOT_FOUND("P001", "Project not found.", HttpStatus.NOT_FOUND),

    SCENE_NOT_FOUND("SC001", "Scene not found.", HttpStatus.NOT_FOUND),
    SCENE_TRANSCRIPT_NOT_FOUND("SCT001", "Scene transcript not found.", HttpStatus.NOT_FOUND),

    TASK_NOT_FOUND("T001", "Task not found.", HttpStatus.NOT_FOUND),

    TEMPLATE_NOT_FOUND("TP001", "Template not found.", HttpStatus.NOT_FOUND),
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
