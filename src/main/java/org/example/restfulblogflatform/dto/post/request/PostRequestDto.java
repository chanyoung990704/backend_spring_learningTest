package org.example.restfulblogflatform.dto.post.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.restfulblogflatform.dto.post.FileValidationUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시글 생성 및 수정 요청 데이터를 담는 DTO(Data Transfer Object)
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // MultipartFile 처리를 위해 기본 생성자 필요
public class PostRequestDto {

    @NotBlank(message = "제목은 필수 입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private List<MultipartFile> files;

    @AssertTrue(message = "파일 크기는 10MB를 초과할 수 없습니다.")
    private boolean isValidFileSize() {
        if (files == null || files.isEmpty()) {
            return true;
        }
        return files.stream()
                .allMatch(file -> file.getSize() <= 10 * 1024 * 1024); // 10MB 제한
    }

    @AssertTrue(message = "허용되지 않는 파일 형식입니다.")
    private boolean isValidFileType() {
        if (files == null || files.isEmpty()) {
            return true;
        }
        return files.stream()
                .allMatch(file -> FileValidationUtil.isAllowedMimeType(file.getContentType()));
    }
}

