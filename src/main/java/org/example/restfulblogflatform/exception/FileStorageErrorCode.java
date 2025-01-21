package org.example.restfulblogflatform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 파일 저장소와 관련된 에러 코드 정의.
 * 파일 업로드, 다운로드, 삭제 등의 작업에서 발생할 수 있는 에러를 관리합니다.
 */
@Getter
@AllArgsConstructor
public enum FileStorageErrorCode {

    /**
     * 파일 업로드가 비활성화된 경우 발생하는 에러.
     */
    FILE_UPLOAD_DISABLED("파일 업로드가 비활성화되어 있습니다."),

    /**
     * 파일 크기가 허용된 최대 크기를 초과한 경우 발생하는 에러.
     */
    FILE_SIZE_EXCEEDS_LIMIT("파일 크기가 허용된 최대 크기를 초과했습니다."),

    /**
     * 파일 형식이 허용되지 않은 경우 발생하는 에러.
     */
    INVALID_FILE_TYPE("허용되지 않는 파일 형식입니다."),

    /**
     * 파일 저장 중 오류가 발생한 경우 발생하는 에러.
     */
    FILE_SAVE_FAILED("파일 저장에 실패했습니다."),

    /**
     * 요청한 파일을 찾을 수 없는 경우 발생하는 에러.
     */
    FILE_NOT_FOUND("요청한 파일을 찾을 수 없습니다."),

    /**
     * 파일 삭제 중 오류가 발생한 경우 발생하는 에러.
     */
    FILE_DELETE_FAILED("파일 삭제에 실패했습니다.");

    private final String message; // 사용자 친화적인 에러 메시지
}