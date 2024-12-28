package org.example.restfulblogflatform.dto.comment.response;

import lombok.Getter;
import lombok.Setter;
import org.example.restfulblogflatform.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {
    private Long id; // 댓글 ID
    private String content; // 댓글 내용
    private String username; // 작성자 이름
    private LocalDateTime createAt; // 댓글 작성날짜

    /**
     * 정적 팩토리 메서드: Comment 엔티티를 CommentResponseDto로 변환합니다.
     *
     * @param comment 댓글 엔티티
     * @return 변환된 CommentResponseDto 객체
     */
    public static CommentResponseDto of(Comment comment) {
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(comment.getId());
        responseDto.setContent(comment.getContent());
        responseDto.setUsername(comment.getUser().getUsername());
        responseDto.setCreateAt(comment.getCreatedDate());
        return responseDto;
    }
}