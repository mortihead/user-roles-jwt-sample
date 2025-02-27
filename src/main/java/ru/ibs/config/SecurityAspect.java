package ru.ibs.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Before("@annotation(preAuthorize)")
    public void logBefore(JoinPoint joinPoint, PreAuthorize preAuthorize) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Проверка доступа к методу: {} с выражением: {}", methodName, preAuthorize.value());
    }

    @AfterReturning("@annotation(preAuthorize)")
    public void logAfterReturning(JoinPoint joinPoint, PreAuthorize preAuthorize) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Метод {} выполнен успешно", methodName);
    }
}