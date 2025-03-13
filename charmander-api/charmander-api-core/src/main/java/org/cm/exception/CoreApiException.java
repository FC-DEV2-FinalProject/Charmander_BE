package org.cm.exception;

import org.cm.common.exception.BaseException;
import org.cm.common.exception.ExceptionCode;

public class CoreApiException extends BaseException {

    public CoreApiException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
