package org.example.restfulblogflatform.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.example.restfulblogflatform.service.post.PostService;
import org.example.restfulblogflatform.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글(Comment) 관련 비즈니스 로직을 처리하는 서비스 구현체.
 *
 * 이 클래스는 댓글 생성, 조회, 삭제와 같은 주요 기능을 제공합니다.
 * 데이터베이스와 상호작용하며, 비즈니스 로직과 데이터 검증을 수행합니다.
 */
@Service // Spring의 Service 계층으로 등록
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class CommentServiceImpl implements CommentService {

    private final PostService postService; // 게시글 관련 비즈니스 로직 처리
    private final UserService userService; // 사용자 관련 비즈니스 로직 처리
    private final CommentRepository commentRepository; // 댓글 데이터 처리 JPA Repository
    private final CommentValidator commentValidator; // 댓글 검증 로직

    /**
     * 댓글 생성 메서드
     *
     * 이 메서드는 특정 게시글에 새로운 댓글을 추가합니다.
     * 게시글과 사용자 정보를 조회한 후, 댓글 객체를 생성하고 저장합니다.
     *
     * @param postId 댓글이 달릴 게시글의 ID (댓글이 속할 게시글)
     * @param userId 댓글 작성자의 사용자 ID (현재 인증된 사용자)
     * @param content 댓글 내용 (사용자가 입력한 텍스트)
     * @return 생성된 댓글(Comment) 객체 (엔티티 형태로 반환)
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public Comment add(Long postId, Long userId, String content) {
        // 게시글과 사용자 정보 가져오기
        Post post = postService.get(postId); // 게시글 조회 (존재하지 않으면 예외 발생)
        User user = userService.get(userId); // 사용자 조회 (존재하지 않으면 예외 발생)

        // 댓글 생성 및 양방향 연관 관계 설정
        Comment comment = Comment.createComment(user, post, content);

        // 댓글 저장 후 반환
        return commentRepository.save(comment);
    }

    /**
     * 댓글 삭제 메서드
     *
     * 이 메서드는 특정 댓글을 삭제합니다.
     * 삭제하려는 댓글이 존재하지 않거나 권한이 없는 경우 예외를 발생시킵니다.
     *
     * @param commentId 삭제할 댓글의 ID (고유 식별자)
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public void delete(Long commentId) {
        // 댓글 가져오기 및 검증 (존재하지 않으면 예외 발생)
        Comment comment = commentValidator.getCommentOrThrow(commentId);

        // 양방향 연관 관계 해제 (게시글에서 댓글 제거)
        comment.getPost().removeComment(comment);

        // 데이터베이스에서 댓글 삭제
        commentRepository.delete(comment);
    }

    /**
     * 특정 게시글에 달린 모든 댓글 조회 메서드
     *
     * 이 메서드는 특정 게시글에 속한 모든 댓글을 조회하여 반환합니다.
     * 반환되는 데이터는 클라이언트에 전달하기 위해 DTO 형태로 변환됩니다.
     *
     * @param postId 조회할 게시글의 ID (댓글이 속한 대상 게시글)
     * @return 해당 게시글에 달린 모든 댓글 리스트 (DTO 형태로 반환)
     */
    @Override
    public List<CommentResponseDto> getAll(Long postId) {
        // 특정 게시글 ID에 해당하는 모든 댓글 조회
        List<Comment> comments = commentRepository.findByPostId(postId);

        // 엔티티(Comment)를 DTO(CommentResponseDto)로 변환하여 반환
        return comments.stream()
                .map(comment -> CommentResponseDto.of(comment))
                .collect(Collectors.toList());
    }
}
