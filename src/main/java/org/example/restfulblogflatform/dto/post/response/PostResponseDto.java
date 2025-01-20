package org.example.restfulblogflatform.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.restfulblogflatform.entity.FileAttachment;
import org.example.restfulblogflatform.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 응답 데이터를 담는 DTO(Data Transfer Object).
 * 클라이언트에게 게시글 정보와 첨부 파일 정보를 반환할 때 사용됩니다.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String username;
    private Long userId;
    private LocalDateTime createdAt;
    private int viewCount;
    private List<FileAttachmentDto> attachments; // 첨부 파일 목록

    /**
     * 첨부 파일 정보를 담는 내부 클래스
     */
    @Getter
    @AllArgsConstructor
    public static class FileAttachmentDto {
        private Long id;
        private String originalFileName;
        private String storedFileName;
        private Long fileSize;
        private String fileType;

        // 필요한 경우, 파일 접근 경로(또는 URL)를 응답에 포함할 수 있습니다.
        private String filePath;

        public static FileAttachmentDto from(FileAttachment attachment) {
            return new FileAttachmentDto(
                    attachment.getId(),
                    attachment.getOriginalFileName(),
                    attachment.getStoredFileName(),
                    attachment.getFileSize(),
                    attachment.getFileType(),
                    attachment.getFilePath() // filePath 추가
            );
        }
    }

    /**
     * Post 엔티티를 PostResponse DTO로 변환하는 정적 팩토리 메서드.
     *
     * @param post Post 엔티티 객체
     * @return PostResponseDto - 변환된 응답 객체
     */
    public static PostResponseDto of(Post post) {
        List<FileAttachmentDto> attachmentDtos = post.getAttachments().stream()
                .map(FileAttachmentDto::from)
                .collect(Collectors.toList());

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getUser().getId(),
                post.getCreatedDate(),
                post.getViewCount(),
                attachmentDtos
        );
    }
}


