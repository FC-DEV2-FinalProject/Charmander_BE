package org.cm.api;

import lombok.extern.slf4j.Slf4j;
import org.cm.common.exception.BaseException;
import org.cm.common.exception.CoreException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class CoreExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleCoreException(BaseException e) {
        printLog(e);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(
                        e.getExceptionCode().getErrorCode(),
                        e.getMessage())
                );
    }
    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ExceptionResponse> handleCoreException(CoreException e) {
        printLog(e);

        var exceptionResponse = new ExceptionResponse(
                e.getWebExceptionCode().getErrorCode(),
                e.getMessage()
        );
        return ResponseEntity.status(e.getWebExceptionCode().httpStatusCode())
                .body(exceptionResponse);
    }

    private void printLog(BaseException e) {
        switch (e.getExceptionCode().getLevel()){
            case ERROR -> log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage(), e);
            case WARN -> log.warn("{} : {}", e.getClass().getSimpleName(), e.getMessage(), e);
            default -> log.info("{} : {}", e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("NoResourceFoundException : {}", e.getMessage());
        var exceptionResponse = new ExceptionResponse("NO_RESOURCE_FOUND", "해당 리소스를 찾을 수 없습니다.");
        return ResponseEntity.status(404).body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        var exceptionResponse = new ExceptionResponse("SERVER_ERROR", "서버에서 에러가 발생하여 처리할 수 없습니다.");
        return ResponseEntity.status(500).body(exceptionResponse);
    }

    public record ExceptionResponse(
            String exceptionCode,
            String message
    ){
    }

}
