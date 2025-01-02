package org.example.restfulblogflatform.service.comment;

import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
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
import org.springframework.data.domain.*;

import java.util.Arrays;

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

    @Test
    @DisplayName("댓글 생성 성공 테스트")
    void addCommentSuccess() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "This is a test comment";

        // Mock 객체 생성 및 반환값 설정
        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);
        Comment mockComment = mock(Comment.class);

        given(postService.get(postId)).willReturn(mockPost);       // 게시글 반환
        given(userService.get(userId)).willReturn(mockUser);       // 사용자 반환
        given(commentRepository.save(any(Comment.class))).willReturn(mockComment); // 저장 동작 Mock

        // Mock 동작 설정 (User, Comment 반환값)
        given(mockComment.getContent()).willReturn(content);       // 댓글 내용 반환 설정
        given(mockComment.getUser()).willReturn(mockUser);         // 댓글 작성자 반환 설정
        given(mockUser.getUsername()).willReturn("user1");         // 작성자 이름 반환 설정

        // when
        CommentResponseDto result = commentService.add(postId, userId, content);

        // then
        assertNotNull(result);                                     // 결과가 null이 아님 검증
        assertEquals(content, result.getContent());                // 댓글 내용 검증
        assertEquals("user1", result.getUsername());               // 작성자 이름 검증

        // Mock 객체 호출 검증
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
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "This is a test comment";

        given(postService.get(postId)).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

        // when & then
        PostException exception = assertThrows(PostException.class, () -> commentService.add(postId, userId, content));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());

        verify(postService).get(postId);
        verify(userService, never()).get(anyLong());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    /**
     * 특정 게시글에 달린 댓글 목록 페이징 조회 성공 테스트
     */
    @Test
    @DisplayName("특정 게시글에 달린 댓글 목록 페이징 조회 성공 테스트")
    void getAllCommentsWithPagingSuccess() {
        // given
        Long postId = 1L;

        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);

        Comment mockComment1 = mock(Comment.class);
        Comment mockComment2 = mock(Comment.class);

        given(mockUser1.getUsername()).willReturn("user1");
        given(mockUser2.getUsername()).willReturn("user2");

        given(mockComment1.getUser()).willReturn(mockUser1);
        given(mockComment1.getContent()).willReturn("First comment");

        given(mockComment2.getUser()).willReturn(mockUser2);
        given(mockComment2.getContent()).willReturn("Second comment");

        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").descending());
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(mockComment1, mockComment2), pageable, 2);

        given(commentRepository.findByPostId(postId, pageable)).willReturn(commentPage);

        // when
        Page<CommentResponseDto> results = commentService.getAll(postId, pageable);

        // then
        assertNotNull(results);
        assertEquals(2, results.getTotalElements());
        assertEquals("First comment", results.getContent().get(0).getContent());
        assertEquals("Second comment", results.getContent().get(1).getContent());
        assertEquals("user1", results.getContent().get(0).getUsername());
        assertEquals("user2", results.getContent().get(1).getUsername());

        verify(commentRepository).findByPostId(postId, pageable);
    }

    /**
     * 댓글 삭제 성공 테스트
     */
    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteCommentSuccess() {
        // given
        Long commentId = 1L;

        Comment mockComment = mock(Comment.class);
        Post mockPost = mock(Post.class);

        given(mockComment.getPost()).willReturn(mockPost);
        given(commentValidator.getCommentOrThrow(commentId)).willReturn(mockComment);

        // when
        commentService.delete(commentId);

        // then
        verify(commentValidator).getCommentOrThrow(commentId);
        verify(mockPost).removeComment(mockComment);
    }
}
