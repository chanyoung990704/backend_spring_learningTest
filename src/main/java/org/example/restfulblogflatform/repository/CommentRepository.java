package org.example.restfulblogflatform.repository;

import org.example.restfulblogflatform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 특정 게시글 ID에 해당하는 모든 댓글을 조회합니다.
     * @param postId 게시글 ID
     * @return 댓글 리스트
     */
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdDate ASC")
    List<Comment> findByPostId(@Param("postId") Long postId);
}