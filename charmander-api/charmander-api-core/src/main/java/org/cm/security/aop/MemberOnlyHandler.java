package org.cm.security.aop;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MemberOnlyHandler {
    @Before("@annotation(org.cm.security.annotations.support.MemberOnly)")
    public void check(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
            .filter(arg -> arg.getClass().isAssignableFrom(AuthInfo.class))
            .map(AuthInfo.class::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unauthorized"));
    }
}
