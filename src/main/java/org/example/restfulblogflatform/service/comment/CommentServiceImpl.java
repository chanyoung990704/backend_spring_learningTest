package org.example.restfulblogflatform.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.example.restfulblogflatform.service.post.PostService;
import org.example.restfulblogflatform.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 댓글(Comment) 관련 비즈니스 로직을 처리하는 서비스 구현체.
 * 댓글 생성 및 삭제와 같은 주요 기능을 제공합니다.
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
     * 댓글을 생성합니다.
     *
     * @param postId 댓글이 달릴 게시글의 ID
     * @param userId 댓글 작성자의 사용자 ID
     * @param content 댓글 내용
     * @return 생성된 댓글(Comment) 객체
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public Comment add(Long postId, Long userId, String content) {
        // 게시글과 사용자 정보 가져오기
        Post post = postService.get(postId); // 게시글 조회
        User user = userService.get(userId); // 사용자 조회

        // 댓글 생성 및 양방향 연관 관계 설정
        Comment comment = Comment.createComment(user, post, content);

        // 댓글 저장 후 반환
        return commentRepository.save(comment);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 ID
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public void delete(Long commentId) {
        // 댓글 가져오기 및 검증 (존재하지 않으면 예외 발생)
        Comment comment = commentValidator.getCommentOrThrow(commentId);

        // 양방향 연관 관계 해제 (게시글에서 댓글 제거)
        comment.getPost().removeComment(comment);
    }
}