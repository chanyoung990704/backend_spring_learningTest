package org.example.restfulblogflatform.service.comment;

import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;

import java.util.List;

/**
 * 댓글(Comment) 관련 비즈니스 로직을 처리하기 위한 서비스 인터페이스.
 *
 * 이 인터페이스는 댓글 생성, 조회, 삭제와 같은 주요 기능을 정의합니다.
 * 구현체는 데이터베이스와의 상호작용 및 비즈니스 로직을 처리합니다.
 */
public interface CommentService {

    /**
     * 댓글 생성 메서드
     *
     * 이 메서드는 특정 게시글에 새로운 댓글을 추가합니다.
     * 댓글 작성자는 인증된 사용자여야 하며, 게시글 ID와 댓글 내용을 기반으로 댓글이 생성됩니다.
     *
     * @param postId 댓글이 달릴 게시글의 ID (댓글이 속할 게시글)
     * @param userId 댓글 작성자의 사용자 ID (현재 인증된 사용자)
     * @param content 댓글 내용 (사용자가 입력한 텍스트)
     * @return 생성된 댓글(Comment) 객체 (엔티티 형태로 반환)
     */
    Comment add(Long postId, Long userId, String content);

    /**
     * 댓글 삭제 메서드
     *
     * 이 메서드는 특정 댓글을 삭제합니다.
     * 삭제하려는 댓글은 존재해야 하며, 삭제 권한은 작성자 또는 관리자에게만 있습니다.
     *
     * @param commentId 삭제할 댓글의 ID (고유 식별자)
     */
    void delete(Long commentId);

    /**
     * 특정 게시글에 달린 모든 댓글 조회 메서드
     *
     * 이 메서드는 특정 게시글에 속한 모든 댓글을 조회하여 반환합니다.
     * 반환되는 데이터는 클라이언트에 전달하기 위해 DTO 형태로 변환됩니다.
     *
     * @param postId 조회할 게시글의 ID (댓글이 속한 대상 게시글)
     * @return 해당 게시글에 달린 모든 댓글 리스트 (DTO 형태로 반환)
     */
    List<CommentResponseDto> getAll(Long postId);
}