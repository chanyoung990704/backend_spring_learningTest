package org.example.restfulblogflatform.exception.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 에러 응답 데이터를 담는 DTO(Data Transfer Object).
 * 클라이언트에게 예외 발생 시 반환할 표준화된 응답 구조를 정의합니다.
 *
 * @param <T> 추가적인 데이터 타입 (예: 유효성 검증 오류 목록)
 */
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
@Builder // Lombok 어노테이션: 빌더 패턴을 사용하여 객체를 생성할 수 있도록 지원
public class ErrorResponse<T> {

    private final HttpStatus status; // HTTP 상태 코드
    private final String message; // 에러 메시지
    private final T data; // 추가적인 데이터 (예: 유효성 검증 오류 정보)
    private final LocalDateTime timestamp; // 에러 발생 시간

}