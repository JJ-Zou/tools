package com.zjj.middleware;

import com.zjj.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class ApiRequestAspect {

    @Autowired
    private AlertService alertService;

    @Pointcut("execution(* com.zjj.controller.*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        log.info("method: [{}] execute start, args: {}.", joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        try {
            final Object result = joinPoint.proceed();
            final long end = System.currentTimeMillis();
            log.info("method: [{}] execute success, return value: [{}], consume {} ms.", joinPoint.getSignature().getName(), result, end - start);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            final long end = System.currentTimeMillis();
            log.error("\"method: [{}] execute fail, exception: [{}], consume {} ms.",
                    Arrays.toString(joinPoint.getArgs()), e, end - start);
            alertService.alert(e.toString());
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }
}
