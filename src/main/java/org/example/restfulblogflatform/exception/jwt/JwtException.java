package org.example.restfulblogflatform.exception.jwt;

import lombok.Getter;
import org.example.restfulblogflatform.exception.JwtErrorCode;

/**
 * JWT 관련 커스텀 예외 클래스들
 */
@Getter
public class JwtException extends RuntimeException {
    private final JwtErrorCode errorCode;

    public JwtException(JwtErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}