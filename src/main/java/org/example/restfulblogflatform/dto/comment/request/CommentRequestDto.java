package org.example.restfulblogflatform.dto.comment.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long userId; // 댓글 작성자의 사용자 ID
    private String content; // 댓글 내용
}