package org.example.restfulblogflatform.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.restfulblogflatform.log.service.LogService;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ServiceAspect {

    private final LogService logService;

    @Around("execution(* org.example.restfulblogflatform.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            String message = String.format("Method %s executed in %d ms",
                    joinPoint.getSignature(), executionTime);
            log.info(message);

            // 성공 로그 저장
            logService.saveLog("INFO", message, null);

            return result;
        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;

            String errorMessage = String.format("Exception in method %s after %d ms: %s",
                    joinPoint.getSignature(), executionTime, ex.getMessage());
            log.error(errorMessage);

            // 실패 로그 저장
            logService.saveLog("ERROR", errorMessage, ex.getMessage());

            throw ex;
        }
    }

}
