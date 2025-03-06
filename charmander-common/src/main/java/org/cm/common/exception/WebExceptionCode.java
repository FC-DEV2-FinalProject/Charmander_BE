package org.cm.common.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatusCode;

public interface WebExceptionCode extends ExceptionCode{
   LogLevel logLevel();
   HttpStatusCode httpStatusCode();
}
