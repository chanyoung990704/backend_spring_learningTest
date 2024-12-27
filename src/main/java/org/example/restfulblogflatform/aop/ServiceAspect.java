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

    // 로그 데이터를 저장하는 서비스 (DI를 통해 주입)
    private final LogService logService;

    /**
     * org.example.restfulblogflatform.service 패키지 및 하위 패키지의 모든 메서드 실행 시
     * 이 Aspect가 적용되도록 설정.
     */
    @Around("execution(* org.example.restfulblogflatform.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 실행 시작 시간을 기록
        long startTime = System.currentTimeMillis();

        try {
            // 대상 메서드 실행
            Object result = joinPoint.proceed();

            // 메서드 실행 시간 계산
            long executionTime = System.currentTimeMillis() - startTime;

            // 성공적으로 실행된 메서드에 대한 로그 메시지 생성
            String successMessage = String.format("Method %s executed in %d ms",
                    joinPoint.getSignature(), executionTime);

            // 로그를 콘솔에 출력
            log.info(successMessage);

            // 로그를 LogService를 통해 저장 (로그 레벨: INFO)
            logService.saveLog("INFO", successMessage, null);

            // 대상 메서드의 결과 반환
            return result;
        } catch (Exception ex) {
            // 예외 발생 시 로깅하지 않고 예외를 그대로 호출자에게 전달
            throw ex;
        }
    }
}
