package org.example.restfulblogflatform.exception.user;

import lombok.Getter;
import org.example.restfulblogflatform.exception.ErrorCode;


@Getter
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}