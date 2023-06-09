package com.zjj.middleware;

import com.datarangers.collector.EventCollector;
import com.datarangers.event.Header;
import com.datarangers.event.HeaderV3;
import com.zjj.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Aspect
public class ApiRequestAspect {

    @Autowired
    private AlertService alertService;

    @Autowired()
    @Qualifier("webEventCollector")
    private EventCollector webEventCollector;

    @Pointcut("execution(* com.zjj.controller.*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String methodArgs = Arrays.toString(joinPoint.getArgs());
        log.info("method: [{}] execute start, args: {}.", methodName,
                methodArgs);
        boolean success = false;
        try {
            final Object result = joinPoint.proceed();
            final long end = System.currentTimeMillis();
            log.info("method: [{}] execute success, return value: [{}], consume {} ms.", methodName, result, end - start);
            success = true;
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            final long end = System.currentTimeMillis();
            log.error("\"method: [{}] execute fail, exception: [{}], consume {} ms.",
                    methodName, e, end - start);
            alertService.alert(e.toString());
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST.getReasonPhrase());
        } finally {
            Header header = HeaderV3.Builder.getInstance()
                    .setClientIp(Inet4Address.getLocalHost().getHostAddress())
                    .setOsName(System.getProperty("os.name"))
                    .setAppId(401595)
                    .setUserUniqueId(this.getClass().getName())
                    .build();
            Map<String, Object> eventParams = new HashMap<>();
            eventParams.put("current_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));
            eventParams.put("http_method_name", methodName);
            eventParams.put("http_method_args", methodArgs);
            eventParams.put("http_invoke_status", success);
            webEventCollector.sendEvent(header, "api_request_aspect", eventParams);
        }
    }
}
