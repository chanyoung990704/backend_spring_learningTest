package org.example.restfulblogflatform.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.CommentException;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.springframework.stereotype.Component;

/**
 * 댓글 관련 검증 로직을 처리하는 Validator 클래스
 *
 * @Component: Spring의 Bean으로 등록되어 의존성 주입 가능
 * @RequiredArgsConstructor: final 필드를 포함한 생성자 자동 생성
 */
@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final CommentRepository commentRepository; // 댓글 데이터베이스 접근 객체

    /**
     * 특정 댓글이 존재하는지 검증합니다.
     *
     * @param commentId 검증할 댓글의 고유 ID
     * @throws CommentException COMMENT_NOT_FOUND 예외를 던집니다. (댓글이 존재하지 않을 경우)
     */
    public void validateCommentExists(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentException(ErrorCode.COMMENT_NOT_FOUND); // 댓글 미존재 예외 처리
        }
    }

    /**
     * 특정 댓글을 조회하고, 존재하지 않을 경우 예외를 던집니다.
     *
     * @param commentId 조회할 댓글의 고유 ID
     * @return 조회된 댓글 엔티티
     * @throws CommentException COMMENT_NOT_FOUND 예외를 던집니다. (댓글이 존재하지 않을 경우)
     */
    public Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND)); // 댓글 미존재 예외 처리
    }
}

