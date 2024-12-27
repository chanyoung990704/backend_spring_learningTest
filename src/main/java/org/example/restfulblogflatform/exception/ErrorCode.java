package org.example.restfulblogflatform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User 관련 에러
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL("이미 사용 중인 이메일입니다."),
    USER_DELETE_FAILED("사용자 삭제에 실패했습니다."),

    // Post 관련 에러
    POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    POST_VALIDATION_FAILED("게시글 검증에 실패했습니다."),
    POST_CREATE_FAILED("게시글 생성에 실패했습니다."),
    POST_UPDATE_FAILED("게시글 업데이트에 실패했습니다."),
    POST_DELETE_FAILED("게시글 삭제에 실패했습니다."),

    // Comment 관련 에러
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다."),
    COMMENT_VALIDATION_FAILED("댓글 검증에 실패했습니다."),
    COMMENT_CREATE_FAILED("댓글 생성에 실패했습니다."),
    COMMENT_DELETE_FAILED("댓글 삭제에 실패했습니다.");

    private final String message;
}
