package org.example.restfulblogflatform.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.example.restfulblogflatform.service.post.PostService;
import org.example.restfulblogflatform.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 댓글(Comment) 관련 비즈니스 로직을 처리하는 서비스 구현체
 *
 * 이 클래스는 댓글 생성, 조회, 수정, 삭제 등의 주요 기능을 제공합니다.
 * 데이터베이스와 상호작용하며, 비즈니스 로직과 데이터 검증을 수행합니다.
 *
 * <p> 주요 기능: </p>
 * - 댓글 생성: 특정 게시글에 댓글을 추가
 * - 댓글 조회: 게시글의 댓글 목록 페이징 처리
 * - 댓글 수정: 작성자가 댓글 내용을 수정
 * - 댓글 삭제: 작성자가 댓글 삭제
 */
@Service // Spring의 Service 계층으로 등록
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class CommentServiceImpl implements CommentService {

    private final PostService postService; // 게시글 관련 비즈니스 로직 처리
    private final UserService userService; // 사용자 관련 비즈니스 로직 처리
    private final CommentRepository commentRepository; // Comment 엔티티 관련 DB 레포지토리
    private final CommentValidator commentValidator; // 댓글 검증 로직을 담당하는 Validator

    /**
     * 댓글 생성 메서드
     *
     * 특정 게시글에 댓글을 추가합니다. 댓글 작성자를 확인하고, 게시글에 댓글을 저장합니다.
     * 저장된 댓글은 DTO 형태로 변환하여 반환됩니다.
     *
     * @param postId  댓글을 작성할 게시글의 ID
     * @param userId  댓글을 작성하는 사용자의 ID
     * @param content 댓글 내용
     * @return 생성된 댓글을 DTO로 반환
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public CommentResponseDto add(Long postId, Long userId, String content) {
        Post post = postService.get(postId); // 게시글 조회 (없는 경우 예외 발생)
        User user = userService.get(userId); // 사용자 조회 (없는 경우 예외 발생)

        Comment comment = Comment.createComment(user, post, content); // 댓글 생성

        return CommentResponseDto.of(commentRepository.save(comment)); // 저장 후 DTO 반환
    }

    /**
     * 단일 댓글 조회 메서드
     *
     * 특정 댓글 ID로 댓글을 조회합니다. 댓글이 존재하지 않는 경우 예외 발생.
     *
     * @param commentId 조회할 댓글의 ID
     * @return 조회된 댓글 엔티티
     */
    @Override
    public Comment get(Long commentId) {
        return commentValidator.getCommentOrThrow(commentId); // 검증 후 댓글 반환
    }

    /**
     * 특정 게시글에 달린 댓글 목록 조회 메서드
     *
     * 특정 게시글 ID에 해당하는 댓글을 페이징 처리하여 조회합니다.
     * 반환 데이터는 DTO로 변환되며, 페이징 정보를 포함합니다.
     *
     * @param postId   댓글을 조회할 게시글의 ID
     * @param pageable 페이징 요청 정보 (페이지 번호, 크기, 정렬)
     * @return 페이징 처리된 댓글 목록 (DTO 형태)
     */
    @Override
    public Page<CommentResponseDto> getAll(Long postId, Pageable pageable) {
        Page<Comment> commentsPage = commentRepository.findByPostId(postId, pageable);
        return commentsPage.map(CommentResponseDto::of); // map 메서드로 DTO 변환
    }

    /**
     * 댓글 수정 메서드
     *
     * 특정 댓글의 내용을 업데이트합니다. 사용자가 작성자인지 확인 후, 댓글의 내용만 변경합니다.
     *
     * @param commentId 수정할 댓글의 ID
     * @param userId    수정 요청을 보낸 사용자의 ID (권한 확인용)
     * @param content   새로운 댓글 내용
     * @return 수정된 댓글을 DTO로 반환
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public CommentResponseDto update(Long commentId, Long userId, String content) {
        Comment comment = commentValidator.getCommentOrThrow(commentId); // 댓글 검증

        comment.updateContent(content); // 댓글 내용 수정

        return CommentResponseDto.of(comment); // 수정된 댓글 DTO 반환
    }

    /**
     * 댓글 삭제 메서드
     *
     * 특정 댓글을 삭제합니다. 댓글 작성자인지 확인 후 삭제를 수행합니다.
     * 삭제 시 게시글-댓글 간 양방향 연관 관계를 정리합니다.
     *
     * @param commentId 삭제할 댓글의 ID
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public void delete(Long commentId) {
        Comment comment = commentValidator.getCommentOrThrow(commentId); // 댓글 검증

        comment.getPost().removeComment(comment); // 게시글에서 댓글 제거 (연관 관계 해제)
    }
}

