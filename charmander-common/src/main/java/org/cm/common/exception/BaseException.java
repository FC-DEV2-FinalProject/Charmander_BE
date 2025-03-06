package org.cm.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public BaseException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
