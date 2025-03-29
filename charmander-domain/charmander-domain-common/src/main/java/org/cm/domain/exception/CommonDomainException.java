package org.cm.domain.exception;

import org.cm.common.exception.BaseException;
import org.cm.common.exception.ExceptionCode;

public class CommonDomainException extends BaseException {

    public CommonDomainException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
