package org.example.restfulblogflatform.exception.handler;

import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.restfulblogflatform.exception.business.CommentException;
import org.example.restfulblogflatform.exception.business.PostException;
import org.example.restfulblogflatform.exception.jwt.JwtException;
import org.example.restfulblogflatform.exception.response.ErrorResponse;
import org.example.restfulblogflatform.exception.business.UserException;
import org.example.restfulblogflatform.log.service.LogService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 전역 예외를 처리하기 위한 컨트롤러 어드바이스 클래스.
 * 애플리케이션에서 발생하는 다양한 예외를 처리하고, 적절한 HTTP 응답을 반환합니다.
 */
@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 처리하기 위한 어드바이스 클래스
@Slf4j // Lombok 어노테이션: 로깅 기능 제공
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성
public class GlobalExceptionHandler {

    private final LogService logService; // 로그를 저장하는 서비스

    /**
     * 인증(Authentication) 관련 예외 처리.
     *
     * @param ex AuthenticationException 객체
     * @return HTTP 401 Unauthorized 응답
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        String errorMessage = String.format("Authentication failed: %s", ex.getMessage());
        saveLog(ex, errorMessage);

        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("인증에 실패했습니다.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 입력값 유효성 검증 실패 예외 처리.
     *
     * @param ex MethodArgumentNotValidException 객체
     * @return HTTP 400 Bad Request 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<List<ValidationError>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        String errorMessage = String.format("입력값 유효 검증 실패 %s", ex.getMessage());
        saveLog(ex, errorMessage);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ValidationError> validationErrors = fieldErrors.stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .toList();

        ErrorResponse<List<ValidationError>> errorResponse = ErrorResponse.<List<ValidationError>>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("입력값 검증에 실패했습니다")
                .data(validationErrors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 데이터 액세스(DataAccess) 관련 예외 처리.
     *
     * @param ex DataAccessException 객체
     * @return HTTP 500 Internal Server Error 응답
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        String errorMessage = String.format("DataSource operation failed: %s", ex.getMessage());
        saveLog(ex, errorMessage);

        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    /**
     * 사용자(User) 관련 예외 처리.
     *
     * @param ex UserException 객체
     * @return HTTP 400 Bad Request 응답
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
        String errorMessage = String.format("User operation failed: %s", ex.getMessage());
        saveLog(ex, errorMessage);

        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .data(ex.getErrorCode().name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 게시글(Post) 관련 예외 처리.
     *
     * @param ex PostException 객체
     * @return HTTP 400 Bad Request 응답
     */
    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> handlePostException(PostException ex) {
        String errorMessage = String.format("Post operation failed: %s", ex.getErrorCode().getMessage());
        saveLog(ex, errorMessage);

        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getErrorCode().getMessage())
                .data(ex.getErrorCode().name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 댓글(Comment) 관련 예외 처리.
     *
     * @param ex CommentException 객체
     * @return HTTP 400 Bad Request 응답
     */
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> handleCommentException(CommentException ex) {
        String errorMessage = String.format("Comment operation failed: %s", ex.getErrorCode().getMessage());
        saveLog(ex, errorMessage);

        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getErrorCode().getMessage())
                .data(ex.getErrorCode().name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * JWT 관련 예외 처리
     *
     * @param ex JwtException 객체
     * @return HTTP 401 Unauthorized 응답
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        String errorMessage = String.format("JWT operation failed: %s", ex.getErrorCode().getMessage());
        saveLog(ex, errorMessage);

        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getErrorCode().getMessage())
                .data(ex.getErrorCode().name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 공통 로그 저장 메서드.
     *
     * @param ex 발생한 예외 객체
     * @param errorMessage 로그로 저장할 에러 메시지
     */
    private void saveLog(Exception ex, String errorMessage) {
        log.error(errorMessage); // 에러 메시지를 로그로 출력
        logService.saveLog("ERROR", errorMessage, ex.getMessage()); // 로그 서비스에 에러 저장
    }

    /**
     * 입력값 유효성 검증 오류 정보를 담는 클래스.
     */
    @Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
    @AllArgsConstructor(access = AccessLevel.PRIVATE) // 모든 필드를 포함하는 생성자를 생성하며, 접근 수준을 PRIVATE로 제한
    private class ValidationError {
        private final String field; // 오류가 발생한 필드 이름
        private final Object rejectedValue; // 거부된 값
        private final String message; // 오류 메시지
    }
}