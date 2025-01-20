package org.example.restfulblogflatform.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file) throws IOException;    // 실제 파일 저장 후, 저장된 파일 이름을 반환
    String getFilePath(String storedFileName);                  // 파일 접근 경로(또는 절대경로) 조회
}
