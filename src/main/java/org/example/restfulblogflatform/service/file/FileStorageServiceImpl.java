package org.example.restfulblogflatform.service.file;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.config.FileProperties;
import org.example.restfulblogflatform.exception.FileStorageErrorCode;
import org.example.restfulblogflatform.exception.file.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * FileStorageService 구현체 (로컬 파일 시스템 기반)
 */
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileProperties fileProperties;

    /**
     * 애플리케이션 시작 시 업로드 디렉토리를 초기화합니다.
     *
     * @throws IOException 디렉토리 생성 실패 시 발생
     */
    @PostConstruct
    public void init() throws IOException {
        Path uploadPath = Paths.get(fileProperties.getLocation());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    /**
     * MultipartFile을 로컬 디스크에 저장합니다.
     *
     * @param file 저장할 MultipartFile 객체
     * @return 저장된 고유한 파일명 (UUID 기반)
     * @throws IOException 입출력 예외 발생 시 처리
     */
    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!fileProperties.isEnabled()) {
            throw new FileStorageException(FileStorageErrorCode.FILE_UPLOAD_DISABLED);
        }

        if (file.getSize() > parseSize(fileProperties.getMaxFileSize())) {
            throw new FileStorageException(FileStorageErrorCode.FILE_SIZE_EXCEEDS_LIMIT);
        }

        try {
            Path uploadPath = Paths.get(fileProperties.getLocation());
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String storedFileName = UUID.randomUUID() + extension;

            Path targetLocation = uploadPath.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return storedFileName;
        } catch (IOException e) {
            throw new FileStorageException(FileStorageErrorCode.FILE_SAVE_FAILED, e);
        }
    }

    /**
     * 저장된 파일의 전체 경로를 반환합니다.
     *
     * @param storedFileName 저장된 고유한 파일명
     * @return 절대 경로 문자열 반환
     */
    @Override
    public String getFilePath(String storedFileName) {
        Path filePath = Paths.get(fileProperties.getLocation())
                .resolve(storedFileName)
                .normalize();
        return filePath.toAbsolutePath().toString();
    }

    /**
     * 주어진 파일명에서 확장자를 추출합니다.
     *
     * @param filename 원본 파일명 (예: "example.pdf")
     * @return 확장자 문자열 (예: ".pdf")
     */
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

    /**
     * 문자열로 표현된 크기를 바이트 단위로 변환합니다.
     *
     * 예: "10MB" -> 10485760 bytes (10 x 1024 x 1024)
     *
     * @param size 변환할 크기 문자열 (예: "10MB", "500KB")
     * @return 바이트 단위 크기 값 반환
     */
    private long parseSize(String size) {
        size = size.toUpperCase();
        if (size.endsWith("KB")) {
            return Long.parseLong(size.substring(0, size.length() - 2)) * 1024;
        } else if (size.endsWith("MB")) {
            return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024;
        }
        return Long.parseLong(size);
    }
}

