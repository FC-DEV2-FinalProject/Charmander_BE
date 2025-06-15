package org.cm.security.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class MemberOnlyHandler {
    @Before("@annotation(org.cm.security.annotations.support.MemberOnly)")
    public void check(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
            .filter((arg) -> arg instanceof AuthInfo)
            .map(AuthInfo.class::cast)
            .findFirst()
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.UNAUTHORIZED));
    }
}
