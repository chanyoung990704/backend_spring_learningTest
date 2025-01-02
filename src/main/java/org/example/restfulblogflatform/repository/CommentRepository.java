package org.example.restfulblogflatform.repository;

import org.example.restfulblogflatform.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시글에 달린 모든 댓글을 조회합니다.
     *
     * @param postId 게시글의 ID
     * @return 게시글에 속한 댓글 리스트 (생성일 기준 오름차순 정렬)
     */
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdDate ASC")
    List<Comment> findByPostId(@Param("postId") Long postId);

    /**
     * 특정 게시글에 달린 댓글을 페이징 처리하여 조회합니다.
     *
     * @param postId   게시글의 ID
     * @param pageable 페이징 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return 페이징 처리된 댓글 리스트
     */
    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
