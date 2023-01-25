package com.example.superheroes.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Aspect
@Component
public class TimerLogger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TimerLogger.class);

    @Around("execution(* *(..)) && @annotation(com.example.superheroes.annotation.Timer)")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        logger.info("className={}, methodName={}, timeMs={}, threadId={}",
                point.getSignature().getDeclaringTypeName(),
                ((MethodSignature) point.getSignature()).getMethod().getName(),
                System.currentTimeMillis() - start,
                Thread.currentThread().getId());
        return result;
    }
}
