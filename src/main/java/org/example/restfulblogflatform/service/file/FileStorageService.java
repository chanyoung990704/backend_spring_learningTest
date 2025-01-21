package org.example.restfulblogflatform.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 파일 저장 및 관리를 위한 서비스 인터페이스
 */
public interface FileStorageService {

    /**
     * MultipartFile을 저장소에 저장하고 저장된 파일명을 반환합니다.
     *
     * @param file 저장할 MultipartFile 객체
     * @return 저장된 파일의 고유한 파일명 (예: "unique-file-name.pdf")
     * @throws IOException 파일 저장 중 발생할 수 있는 입출력 예외
     */
    String storeFile(MultipartFile file) throws IOException;

    /**
     * 저장된 파일의 접근 경로를 반환합니다.
     *
     * @param storedFileName 저장된 파일명
     * @return 절대 경로나 URL 형식의 접근 경로
     */
    String getFilePath(String storedFileName);
}

