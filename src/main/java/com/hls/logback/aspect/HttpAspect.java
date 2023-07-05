package com.hls.logback.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "http-log")
@Aspect
@Component
public class HttpAspect {

    @Pointcut("execution(* com.hls.logback.controller..*.*(..))")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 获取目标参数
        String serviceUniqueName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        long start = System.currentTimeMillis();
        Object proceed = proceedingJoinPoint.proceed();
        log.info("@Http:{}.{},耗时:{}ms", serviceUniqueName, methodName,
                System.currentTimeMillis() - start);
        return proceed;
    }
}
