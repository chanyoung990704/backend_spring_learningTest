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

            // 성공 로깅만 수행
            String successMessage = String.format("Method %s executed in %d ms",
                    joinPoint.getSignature(), executionTime);
            log.info(successMessage);
            logService.saveLog("INFO", successMessage, null);

            return result;
        } catch (Exception ex) {
            // 예외는 로깅하지 않고 그대로 전파
            throw ex;
        }
    }

}
