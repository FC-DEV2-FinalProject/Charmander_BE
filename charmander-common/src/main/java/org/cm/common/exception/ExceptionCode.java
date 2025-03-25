package org.cm.common.exception;

import org.springframework.boot.logging.LogLevel;

public interface ExceptionCode {

    // TODO getCode 명칭 리팩토링하는게 좋을듯?
    String getCode();
    String getMessage();
    LogLevel getLevel();

    /**
     * 재정의해서 사용할 것
     */
    default String getPrefix(){
        throw new UnsupportedOperationException();
    }

    /**
     * PREFIX + code 형색으로 갈 것
     * PREFIX = CODOM (core domain)
     * code = A01
     * ex) CODOM-A01
     */
    default String getErrorCode(){
        return getPrefix() + "-" + getCode();
    }
}
