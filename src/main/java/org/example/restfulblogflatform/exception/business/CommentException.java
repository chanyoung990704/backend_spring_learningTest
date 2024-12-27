package org.example.restfulblogflatform.exception.business;

import lombok.Getter;
import org.example.restfulblogflatform.exception.ErrorCode;

@Getter
public class CommentException extends RuntimeException {
    private final ErrorCode errorCode;

    public CommentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
