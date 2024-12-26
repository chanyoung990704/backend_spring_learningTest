package org.example.restfulblogflatform.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final CommentRepository commentRepository;

    // 댓글 존재 여부 검증
    public void validateCommentExists(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found with id: " + commentId);
        }
    }

    // 댓글 가져오기 (없으면 예외 발생)
    public Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));
    }
}
