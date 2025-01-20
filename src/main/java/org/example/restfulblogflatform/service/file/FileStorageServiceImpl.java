package org.example.restfulblogflatform.service.file;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.config.FileProperties;
import org.example.restfulblogflatform.exception.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final FileProperties fileProperties;

    @PostConstruct
    public void init() throws IOException {
        Path uploadPath = Paths.get(fileProperties.getLocation());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!fileProperties.isEnabled()) {
            throw new FileStorageException("File upload is disabled");
        }

        // 파일 크기 검증
        if (file.getSize() > parseSize(fileProperties.getMaxFileSize())) {
            throw new FileStorageException("File size exceeds maximum limit");
        }

        Path uploadPath = Paths.get(fileProperties.getLocation());

        // 원본 파일 이름에서 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String storedFileName = UUID.randomUUID() + extension;

        // 파일 저장
        Path targetLocation = uploadPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return storedFileName;
    }

    @Override
    public String getFilePath(String storedFileName) {
        Path filePath = Paths.get(fileProperties.getLocation())
                .resolve(storedFileName)
                .normalize();
        return filePath.toAbsolutePath().toString();
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

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
