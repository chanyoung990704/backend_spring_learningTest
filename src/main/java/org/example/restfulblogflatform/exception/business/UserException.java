package org.example.restfulblogflatform.exception.business;

import lombok.Getter;
import org.example.restfulblogflatform.exception.ErrorCode;


/**
 * 사용자(User) 관련 예외를 처리하기 위한 커스텀 예외 클래스.
 * 런타임 예외(RuntimeException)를 상속받아 실행 중 발생하는 사용자 관련 오류를 처리합니다.
 */
@Getter // Lombok 어노테이션: errorCode 필드에 대한 Getter 메서드를 자동 생성
public class UserException extends RuntimeException {

    private final ErrorCode errorCode; // 예외와 관련된 에러 코드

    /**
     * UserException 생성자.
     * 특정 에러 코드를 기반으로 예외를 생성하며, 에러 메시지는 ErrorCode에서 가져옵니다.
     *
     * @param errorCode 에러 코드 객체 (ErrorCode 열거형)
     */
    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 클래스(RuntimeException)의 생성자 호출 (에러 메시지 설정)
        this.errorCode = errorCode; // 에러 코드 설정
    }
}