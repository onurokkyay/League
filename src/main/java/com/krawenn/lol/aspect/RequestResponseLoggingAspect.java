package com.krawenn.lol.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestResponseLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingAspect.class);

    @Around("execution(* com.krawenn.lol.controller..*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Request: {}.{} with arguments = {}", 
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            joinPoint.getArgs());

        Object result = joinPoint.proceed();

        logger.info("Response: {}.{} returned = {}", 
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            result);

        return result;
    }
} 