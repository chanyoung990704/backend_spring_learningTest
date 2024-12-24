package org.example.restfulblogflatform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL("이미 사용 중인 이메일입니다."),
    USER_DELETE_FAILED("사용자 삭제에 실패했습니다.");

    private final String message;
}