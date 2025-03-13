package org.cm.exception;

import org.cm.common.exception.BaseException;
import org.cm.common.exception.ExceptionCode;

public class CoreDomainException extends BaseException {

    public CoreDomainException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
