package org.cm.exception;

import org.cm.common.exception.BaseException;
import org.cm.common.exception.ExceptionCode;

public class CommonApiException extends BaseException {

    public CommonApiException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
