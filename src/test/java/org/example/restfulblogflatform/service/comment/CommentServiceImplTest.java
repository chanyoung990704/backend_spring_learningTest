package org.example.restfulblogflatform.service.comment;

import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.CommentException;
import org.example.restfulblogflatform.exception.business.PostException;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.example.restfulblogflatform.service.post.PostService;
import org.example.restfulblogflatform.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentValidator commentValidator;

    @InjectMocks
    private CommentServiceImpl commentService;

    /**
     * 댓글 생성 성공 테스트
     */
    @Test
    @DisplayName("댓글 생성 성공 테스트")
    void addCommentSuccess() {
        // given: 정상적인 댓글 생성 요청 데이터
        Long postId = 1L;
        Long userId = 1L;
        String content = "This is a test comment";

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);
        Comment mockComment = mock(Comment.class);

        // Mock 동작 정의: 게시글과 사용자 조회 및 댓글 저장
        given(postService.get(postId)).willReturn(mockPost);
        given(userService.get(userId)).willReturn(mockUser);
        given(commentRepository.save(any(Comment.class))).willReturn(mockComment);

        // when: 댓글 생성 서비스 호출
        Comment result = commentService.add(postId, userId, content);

        // then: 결과 검증 및 Mock 객체 동작 확인
        assertNotNull(result);
        verify(postService).get(postId);
        verify(userService).get(userId);
        verify(commentRepository).save(any(Comment.class));
    }

    /**
     * 댓글 생성 실패 테스트 - 게시글 없음
     */
    @Test
    @DisplayName("댓글 생성 실패 테스트 - 게시글 없음")
    void addCommentFailDueToPostNotFound() {
        // given: 존재하지 않는 게시글 ID로 요청 생성
        Long postId = 1L;
        Long userId = 1L;
        String content = "This is a test comment";

        // Mock 동작 정의: 게시글 조회 시 예외 발생
        given(postService.get(postId)).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

        // when & then: 예외 발생 여부 확인 및 검증
        PostException exception = assertThrows(PostException.class, () -> commentService.add(postId, userId, content));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());

        verify(postService).get(postId);
        verify(userService, never()).get(anyLong());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    /**
     * 댓글 삭제 성공 테스트
     */
    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteCommentSuccess() {
        // given: 정상적인 댓글 ID로 요청 생성
        Long commentId = 1L;

        Post mockPost = mock(Post.class);
        Comment mockComment = mock(Comment.class);

        // Mock 동작 정의: 댓글의 연관 관계 설정
        given(mockComment.getPost()).willReturn(mockPost);
        given(commentValidator.getCommentOrThrow(commentId)).willReturn(mockComment);

        // when: 댓글 삭제 서비스 호출
        commentService.delete(commentId);

        // then: Mock 객체 동작 검증 (삭제 호출 확인)
        verify(commentValidator).getCommentOrThrow(commentId);
        verify(mockPost).removeComment(mockComment); // 연관 관계 해제 확인
    }

    /**
     * 댓글 삭제 실패 테스트 - 댓글 없음
     */
    @Test
    @DisplayName("댓글 삭제 실패 테스트 - 댓글 없음")
    void deleteCommentFailDueToNotFound() {
        // given: 존재하지 않는 댓글 ID로 요청 생성
        Long commentId = 1L;

        // Mock 동작 정의: 댓글 조회 시 예외 발생
        given(commentValidator.getCommentOrThrow(commentId)).willThrow(new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        // when & then: 예외 발생 여부 확인 및 검증
        CommentException exception = assertThrows(CommentException.class, () -> commentService.delete(commentId));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());

        verify(commentValidator).getCommentOrThrow(commentId);
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    /**
     * 특정 게시글에 달린 모든 댓글 조회 성공 테스트
     */
    @Test
    @DisplayName("특정 게시글에 달린 모든 댓글 조회 성공 테스트")
    void getAllCommentsSuccess() {
        // given: 특정 게시글 ID로 요청 생성 및 Mock 데이터 준비
        Long postId = 1L;

        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);
        Comment mockComment1 = mock(Comment.class);
        Comment mockComment2 = mock(Comment.class);

        // 필요한 스터빙만 정의
        given(mockComment1.getUser()).willReturn(mockUser1);
        given(mockComment1.getContent()).willReturn("First comment");
        given(mockUser1.getUsername()).willReturn("user1");

        given(mockComment2.getUser()).willReturn(mockUser2);
        given(mockComment2.getContent()).willReturn("Second comment");
        given(mockUser2.getUsername()).willReturn("user2");

        List<Comment> mockComments = Arrays.asList(mockComment1, mockComment2);

        // Mock 동작 정의: 특정 게시글에 대한 모든 댓글 조회 성공 처리
        given(commentRepository.findByPostId(postId)).willReturn(mockComments);

        // when: 모든 댓글 조회 서비스 호출
        List<CommentResponseDto> results = commentService.getAll(postId);

        // then: 결과 검증 및 Mock 객체 동작 확인 (댓글 수와 내용 확인)
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("First comment", results.get(0).getContent());
        assertEquals("Second comment", results.get(1).getContent());
        assertEquals("user1", results.get(0).getUsername());
        assertEquals("user2", results.get(1).getUsername());

        verify(commentRepository).findByPostId(postId);
    }

    /**
     * 댓글 업데이트 성공 테스트
     */
    @Test
    @DisplayName("댓글 업데이트 성공 테스트")
    void updateCommentSuccess() {
        // given: 정상적인 업데이트 요청 데이터 준비
        Long commentId = 1L;
        Long userId = 1L;
        String updatedContent = "Updated content";

        Comment mockComment = mock(Comment.class);

        // Mock 동작 정의: 댓글 조회 성공 처리
        given(commentValidator.getCommentOrThrow(commentId)).willReturn(mockComment);

        // when: 댓글 업데이트 서비스 호출
        Comment result = commentService.update(commentId, userId, updatedContent);

        // then: 결과 검증 및 Mock 객체 동작 확인
        assertNotNull(result);
        verify(commentValidator).getCommentOrThrow(commentId);
        verify(mockComment).updateContent(updatedContent);
    }

    /**
     * 댓글 업데이트 실패 테스트 - 댓글 없음
     */
    @Test
    @DisplayName("댓글 업데이트 실패 테스트 - 댓글 없음")
    void updateCommentFailDueToNotFound() {
        // given: 존재하지 않는 댓글 ID로 요청 생성
        Long commentId = 1L;
        Long userId = 1L;
        String updatedContent = "Updated content";

        // Mock 동작 정의: 댓글 조회 시 예외 발생
        given(commentValidator.getCommentOrThrow(commentId)).willThrow(new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        // when & then: 예외 발생 여부 확인 및 검증
        CommentException exception = assertThrows(CommentException.class, () -> commentService.update(commentId, userId, updatedContent));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());

        verify(commentValidator).getCommentOrThrow(commentId);
        verifyNoInteractions(commentRepository); // 저장소와의 상호작용이 없음을 확인
    }

}
