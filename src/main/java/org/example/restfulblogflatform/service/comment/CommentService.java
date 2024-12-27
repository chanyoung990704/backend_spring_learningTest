package org.example.restfulblogflatform.service.comment;

import org.example.restfulblogflatform.entity.Comment;

/**
 * 댓글(Comment) 관련 비즈니스 로직을 처리하기 위한 서비스 인터페이스.
 * 댓글 생성 및 삭제와 같은 주요 기능을 정의합니다.
 */
public interface CommentService {

    /**
     * 댓글을 생성합니다.
     *
     * @param postId 댓글이 달릴 게시글의 ID
     * @param userId 댓글 작성자의 사용자 ID
     * @param content 댓글 내용
     * @return 생성된 댓글(Comment) 객체
     */
    Comment add(Long postId, Long userId, String content);

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 ID
     */
    void delete(Long commentId);
}
