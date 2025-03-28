package org.cm.security.aop;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
            .map(AuthInfo.class::cast)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unauthorized"));
    }
}
