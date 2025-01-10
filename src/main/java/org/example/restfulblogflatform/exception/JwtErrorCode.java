package org.example.restfulblogflatform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JWT 관련 에러 코드 열거형
 */
@Getter
@AllArgsConstructor
public enum JwtErrorCode {
    TOKEN_MISSING("토큰이 제공되지 않았습니다."),
    INVALID_TOKEN_FORMAT("잘못된 토큰 형식입니다."),
    TOKEN_INVALIDATION_ERROR("토큰 무효화 처리 중 오류가 발생했습니다.");

    private final String message;
}