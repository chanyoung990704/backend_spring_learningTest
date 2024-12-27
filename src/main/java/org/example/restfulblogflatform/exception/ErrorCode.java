package org.example.restfulblogflatform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 애플리케이션 전역에서 사용할 에러 코드 정의.
 * 각 도메인(User, Post, Comment)와 관련된 에러 메시지를 관리합니다.
 */
@Getter // Lombok 어노테이션: message 필드에 대한 Getter 메서드를 자동 생성
@AllArgsConstructor // Lombok 어노테이션: 모든 필드를 포함하는 생성자를 자동 생성
public enum ErrorCode {

    // User 관련 에러
    /**
     * 사용자를 찾을 수 없는 경우 발생하는 에러.
     */
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),

    /**
     * 이메일이 중복된 경우 발생하는 에러.
     */
    DUPLICATE_EMAIL("이미 사용 중인 이메일입니다."),

    /**
     * 사용자 삭제에 실패한 경우 발생하는 에러.
     */
    USER_DELETE_FAILED("사용자 삭제에 실패했습니다."),

    // Post 관련 에러
    /**
     * 게시글을 찾을 수 없는 경우 발생하는 에러.
     */
    POST_NOT_FOUND("게시글을 찾을 수 없습니다."),

    /**
     * 게시글 검증에 실패한 경우 발생하는 에러.
     */
    POST_VALIDATION_FAILED("게시글 검증에 실패했습니다."),

    /**
     * 게시글 생성에 실패한 경우 발생하는 에러.
     */
    POST_CREATE_FAILED("게시글 생성에 실패했습니다."),

    /**
     * 게시글 업데이트에 실패한 경우 발생하는 에러.
     */
    POST_UPDATE_FAILED("게시글 업데이트에 실패했습니다."),

    /**
     * 게시글 삭제에 실패한 경우 발생하는 에러.
     */
    POST_DELETE_FAILED("게시글 삭제에 실패했습니다."),

    // Comment 관련 에러
    /**
     * 댓글을 찾을 수 없는 경우 발생하는 에러.
     */
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다."),

    /**
     * 댓글 검증에 실패한 경우 발생하는 에러.
     */
    COMMENT_VALIDATION_FAILED("댓글 검증에 실패했습니다."),

    /**
     * 댓글 생성에 실패한 경우 발생하는 에러.
     */
    COMMENT_CREATE_FAILED("댓글 생성에 실패했습니다."),

    /**
     * 댓글 삭제에 실패한 경우 발생하는 에러.
     */
    COMMENT_DELETE_FAILED("댓글 삭제에 실패했습니다.");

    private final String message; // 각 에러 코드와 연결된 사용자 친화적인 메시지
}