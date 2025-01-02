package org.example.restfulblogflatform.service.comment;

import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 댓글(Comment) 관련 비즈니스 로직을 정의하는 서비스 인터페이스.
 *
 * 댓글 생성, 조회, 수정, 삭제와 같은 주요 기능을 제공합니다.
 * 이 인터페이스를 구현하는 클래스는 비즈니스 로직을 처리하고 데이터베이스와 상호작용합니다.
 */
public interface CommentService {

    /**
     * 댓글 생성
     *
     * 특정 게시글에 새로운 댓글을 추가합니다. 게시글 ID와 댓글 내용을 기반으로 댓글을 생성하며,
     * 작성자는 인증된 사용자여야 합니다.
     *
     * @param postId  댓글이 달릴 게시글의 ID
     * @param userId  댓글 작성자의 사용자 ID
     * @param content 댓글 내용
     * @return 생성된 댓글 정보를 담은 DTO
     */
    CommentResponseDto add(Long postId, Long userId, String content);

    /**
     * 댓글 단일 조회
     *
     * 특정 댓글 ID를 기반으로 댓글을 조회합니다. 존재하지 않는 경우 예외를 발생시킵니다.
     *
     * @param commentId 조회할 댓글의 ID
     * @return 조회된 댓글 엔티티
     */
    Comment get(Long commentId);

    /**
     * 게시글에 달린 댓글 목록 조회
     *
     * 특정 게시글 ID에 달린 댓글을 페이징 처리하여 조회합니다. 조회된 댓글은 DTO 형태로 반환됩니다.
     *
     * @param postId   댓글을 조회할 게시글의 ID
     * @param pageable 페이징 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return 페이징 처리된 댓글 리스트를 담은 DTO
     */
    Page<CommentResponseDto> getAll(Long postId, Pageable pageable);

    /**
     * 댓글 수정
     *
     * 특정 댓글 ID를 기반으로 댓글 내용을 수정합니다. 수정 권한은 댓글 작성자 또는 관리자에게만 있습니다.
     *
     * @param commentId 수정할 댓글의 ID
     * @param userId    요청을 보낸 사용자 ID (권한 확인용)
     * @param content   수정된 댓글 내용
     * @return 수정된 댓글 정보를 담은 DTO
     */
    CommentResponseDto update(Long commentId, Long userId, String content);

    /**
     * 댓글 삭제
     *
     * 특정 댓글을 삭제합니다. 삭제하려는 댓글이 존재해야 하며, 작성자 또는 관리자만 삭제할 수 있습니다.
     *
     * @param commentId 삭제할 댓글의 ID
     */
    void delete(Long commentId);
}
