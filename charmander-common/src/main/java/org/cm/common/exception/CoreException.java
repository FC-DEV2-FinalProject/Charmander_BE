package org.cm.common.exception;


import lombok.Getter;

@Getter
public class CoreException extends BaseException {
    private final WebExceptionCode webExceptionCode;

    public CoreException(WebExceptionCode webExceptionCode) {
        super(webExceptionCode);
        this.webExceptionCode = webExceptionCode;
    }
}
