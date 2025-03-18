package org.cm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cm.common.exception.ExceptionCode;
import org.springframework.boot.logging.LogLevel;

@Getter
@RequiredArgsConstructor
public enum CoreDomainExceptionCode implements ExceptionCode {

    // TASK
    START_ALLOWED_ONLY_IN_PENDING("A01", "대기 상태만 작업을 시작할 수 있습니다." ),
    SUCCEED_ALLOWED_ONLY_IN_PROGRESS("A02", "작업 진행 중이 아니므로 완료할 수 없습니다."),
    RETRY_ALLOWED_ONLY_IN_FAILED("A03", "작업 재시도는 실패한 경우만 가능합니다." ),
    CANCEL_ALLOWED_PENDING_OR_IN_PROGRESS("A04", "작업 취소는 대기, 진행 상태에서만 가능합니다." ),
    NOT_FOUND_TASK("A05", "작업이 존재하지 않습니다"),
    // Template
    UPDATE_ALLOWED_ONLY_IN_DRAFT("B01", "초안 상태에서만 수정할 수 있습니다."),
    PUBLISH_ALLOWED_ONLY_IN_DRAFT("B02", "초안 상태에서만 공개할 수 있습니다."),
    DISABLE_ALLOWED_ONLY_IN_PUBLIC("B03", "공개 상태에서만 중단할 수 있습니다."),

    // PROJECT
    NOT_FOUND_PROJECT("C01", "프로젝트를 찾을 수 없습니다"),

    ;

    // Core domain
    private static final String PREFIX = "CORDOM";

    private final String code;
    private final String message;
    private final LogLevel level;

    // 예외 코드의 기본 설정은 INFO
    CoreDomainExceptionCode(String code, String message){
        this.code = code;
        this.message = message;
        this.level = LogLevel.INFO;
    }

    @Override
    public String getPrefix() {
        return "CORDOM";
    }
}
