package org.example.restfulblogflatform.exception.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse<T> {
    private final HttpStatus status;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;
}