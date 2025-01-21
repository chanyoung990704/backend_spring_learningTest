package org.example.restfulblogflatform.exception.file;

import org.example.restfulblogflatform.exception.FileStorageErrorCode;

/**
 * 파일 저장소와 관련된 예외 클래스.
 * FileStorageErrorCode를 사용하여 각 에러에 대한 세부 정보를 제공합니다.
 */
public class FileStorageException extends RuntimeException {

    private final FileStorageErrorCode errorCode; // 에러 코드

    /**
     * FileStorageException 생성자 (에러 코드만 전달)
     *
     * @param errorCode 발생한 에러의 코드
     */
    public FileStorageException(FileStorageErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * FileStorageException 생성자 (에러 코드와 원인 전달)
     *
     * @param errorCode 발생한 에러의 코드
     * @param cause 예외의 원인
     */
    public FileStorageException(FileStorageErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * 발생한 예외의 에러 코드를 반환합니다.
     *
     * @return FileStorageErrorCode
     */
    public FileStorageErrorCode getErrorCode() {
        return errorCode;
    }
}