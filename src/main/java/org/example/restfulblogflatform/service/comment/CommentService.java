package org.example.restfulblogflatform.service.comment;

import org.example.restfulblogflatform.entity.Comment;

public interface CommentService {
    Comment addComment(Long postId, Long userId, String content);
    void deleteComment(Long commentId);
}
