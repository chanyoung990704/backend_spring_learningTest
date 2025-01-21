package org.example.restfulblogflatform.dto.post;

import java.util.Arrays;
import java.util.List;

/**
 * 허용된 파일 형식 검증 로직을 별도로 분리하여 관리
 *
 * 파일 형식 검증을 위한 유틸리티 클래스
 */
public class FileValidationUtil {

    /**
     * 허용된 파일 MIME 타입 목록
     */
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/", // 모든 이미지 형식
            "application/pdf", // PDF 파일
            "application/msword", // MS Word (.doc)
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" // MS Word (.docx)
    );

    /**
     * 파일의 MIME 타입이 허용된 형식인지 확인
     *
     * @param contentType 파일의 MIME 타입
     * @return 허용된 형식이면 true, 그렇지 않으면 false
     */
    public static boolean isAllowedMimeType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return ALLOWED_MIME_TYPES.stream()
                .anyMatch(allowedType -> contentType.startsWith(allowedType) || contentType.equals(allowedType));
    }
}
