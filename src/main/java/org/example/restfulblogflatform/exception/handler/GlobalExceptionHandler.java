package org.example.restfulblogflatform.exception.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.restfulblogflatform.exception.business.CommentException;
import org.example.restfulblogflatform.exception.business.PostException;
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

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogService logService;

    // AuthenticationException 처리
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

    // @Valid에서 예외가 발생했을 경우
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

    // 데이터 엑세스 관련 예외 처리
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

    // User Service에서 발생한 예외들
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
        String errorMessage = String.format("User operation failed: %s", ex.getMessage());
        saveLog(ex, errorMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .data(ex.getErrorCode().name())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Post 관련 예외 처리
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

    // Comment 관련 예외 처리
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

    // 공통 로그 저장 메서드
    private void saveLog(Exception ex, String errorMessage) {
        log.error(errorMessage);
        logService.saveLog("ERROR", errorMessage, ex.getMessage());
    }

    // ValidationError 클래스 정의
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private class ValidationError {
        private final String field;
        private final Object rejectedValue;
        private final String message;
    }
}
