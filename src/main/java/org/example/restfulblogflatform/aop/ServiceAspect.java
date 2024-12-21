package org.example.restfulblogflatform.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceAspect {

    @Around("@within(org.springframework.stereotype.Service)") // @Service 어노테이션이 붙은 클래스의 모든 메서드에 적용
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        try {
            Object result = joinPoint.proceed(); // 실제 메서드 실행

            long endTime = System.currentTimeMillis(); // 종료 시간 기록
            long executionTime = endTime - startTime; // 실행 시간 계산

            log.info("Method {} executed successfully in {} ms",
                    joinPoint.getSignature(), executionTime);

            return result; // 결과 반환
        } catch (Exception ex) {
            long endTime = System.currentTimeMillis(); // 예외 발생 시에도 종료 시간 기록
            long executionTime = endTime - startTime; // 실행 시간 계산

            log.error("Exception in method {} after {} ms: {}",
                    joinPoint.getSignature(), executionTime, ex.getMessage());

            throw ex; // 예외 전파
        }
    }

}
