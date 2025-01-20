package org.example.restfulblogflatform.dto.post.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시글 생성 및 수정 요청 데이터를 담는 DTO(Data Transfer Object).
 * 클라이언트가 전달하는 게시글 제목, 내용 및 첨부 파일을 포함하며, 유효성 검증을 수행합니다.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // MultipartFile 처리를 위해 기본 생성자 추가
public class PostRequestDto {

    @NotBlank(message = "제목은 필수 입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private List<MultipartFile> files; // 첨부 파일 목록

    // 파일 관련 유효성 검증
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
                .allMatch(file -> {
                    String contentType = file.getContentType();
                    return contentType != null && (
                            contentType.startsWith("image/") ||
                                    contentType.equals("application/pdf") ||
                                    contentType.equals("application/msword") ||
                                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    );
                });
    }
}
