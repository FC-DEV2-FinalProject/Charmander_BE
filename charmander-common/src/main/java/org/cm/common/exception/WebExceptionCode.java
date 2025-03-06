package org.cm.common.exception;

import org.springframework.http.HttpStatusCode;

public interface WebExceptionCode extends ExceptionCode{
   HttpStatusCode httpStatusCode();
}
