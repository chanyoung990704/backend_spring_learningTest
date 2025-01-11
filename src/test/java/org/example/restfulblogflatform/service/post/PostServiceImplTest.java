package org.example.restfulblogflatform.service.post;

import org.example.restfulblogflatform.dto.post.request.PostRequestDto;
import org.example.restfulblogflatform.dto.post.response.PostResponseDto;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.PostException;
import org.example.restfulblogflatform.exception.business.UserException;
import org.example.restfulblogflatform.repository.PostRepository;
import org.example.restfulblogflatform.service.user.UserService;
import org.example.restfulblogflatform.service.validator.PostValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 지원 활성화
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository; // Mocking된 PostRepository

    @Mock
    private UserService userService; // Mocking된 UserService

    @Mock
    private PostValidator postValidator; // Mocking된 PostValidator

    @InjectMocks
    private PostServiceImpl postService; // 테스트 대상 서비스 구현체

    /**
     * 게시글 생성 성공 테스트
     */
    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void addSuccess() {
        // given
        Long userId = 1L;
        PostRequestDto postRequestDto = new PostRequestDto("Test Title", "Test Content");
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, postRequestDto.getTitle(), postRequestDto.getContent());

        // Mock 동작 정의
        given(userService.get(userId)).willReturn(mockUser);
        given(postRepository.save(any(Post.class))).willReturn(mockPost);

        // when
        PostResponseDto response = postService.add(postRequestDto, userId);

        // then
        assertNotNull(response);
        assertEquals(postRequestDto.getTitle(), response.getTitle());
        assertEquals(postRequestDto.getContent(), response.getContent());

        verify(userService).get(userId);
        verify(postRepository).save(any(Post.class));
    }

    /**
     * 게시글 단일 조회 성공 테스트
     */
    @Test
    @DisplayName("게시글 단일 조회 성공 테스트")
    void getSuccess() {
        // given
        Long postId = 1L;
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Test Title", "Test Content");

        // Mock 동작 정의
        given(postValidator.getOrThrow(postId)).willReturn(mockPost);

        // when
        Post result = postService.get(postId);

        // then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(postValidator).getOrThrow(postId);
    }

    /**
     * 게시글 삭제 성공 테스트
     */
    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void deleteSuccess() {
        // given
        Long postId = 1L;
        User mockUser = mock(User.class);
        Post mockPost = mock(Post.class);

        // Mock 동작 정의
        given(postValidator.getOrThrow(postId)).willReturn(mockPost);
        given(mockPost.getUser()).willReturn(mockUser);

        // when
        postService.delete(postId);

        // then
        verify(postValidator).getOrThrow(postId);
        verify(mockPost).getUser();
        verify(mockUser).removePost(mockPost);
    }

    /**
     * 모든 게시글 조회 성공 테스트
     */
    @Test
    @DisplayName("모든 게시글 조회 성공 테스트")
    void getAllSuccess() {
        // given
        User mockUser1 = User.createUser("user1", "password1", "user1@example.com");
        User mockUser2 = User.createUser("user2", "password2", "user2@example.com");
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> mockPosts = Arrays.asList(
                Post.createPost(mockUser1, "Title 1", "Content 1"),
                Post.createPost(mockUser2, "Title 2", "Content 2")
        );
        Page<Post> mockPage = new PageImpl<>(mockPosts, pageable, mockPosts.size());

        // Mock 동작 정의
        given(postRepository.findAll(pageable)).willReturn(mockPage);

        // when
        Page<PostResponseDto> results = postService.getAll(pageable);

        // then
        assertNotNull(results);
        assertEquals(2, results.getTotalElements());
        assertEquals("Title 1", results.getContent().get(0).getTitle());
        verify(postRepository).findAll(pageable);
    }

    /**
     * 게시글 수정 성공 테스트
     */
    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void updateSuccess() {
        // given
        Long postId = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Original Title", "Original Content");

        // Mock 동작 정의
        given(postValidator.getOrThrow(postId)).willReturn(mockPost);

        // when
        PostResponseDto response = postService.update(postId, updatedTitle, updatedContent);

        // then
        assertNotNull(response);
        assertEquals(updatedTitle, response.getTitle());
        assertEquals(updatedContent, response.getContent());
        verify(postValidator).getOrThrow(postId);
    }

    /**
     * 게시글 생성 실패 테스트 - 사용자 없음
     */
    @Test
    @DisplayName("게시글 생성 실패 테스트 - 사용자 없음")
    void addFailDueToUserNotFound() {
        // given
        Long userId = 1L;
        PostRequestDto postRequestDto = new PostRequestDto("Test Title", "Test Content");

        // Mock 동작 정의
        given(userService.get(userId)).willThrow(new UserException(ErrorCode.USER_NOT_FOUND));

        // when & then
        UserException exception = assertThrows(UserException.class, () -> postService.add(postRequestDto, userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userService).get(userId);
        verify(postRepository, never()).save(any(Post.class));
    }

    /**
     * 게시글 단일 조회 실패 테스트 - 게시글 없음
     */
    @Test
    @DisplayName("게시글 단일 조회 실패 테스트 - 게시글 없음")
    void getPostFailDueToPostNotFound(){
        // given
        Long postId = 1L;
        given(postValidator.getOrThrow(postId)).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));
        // when & then
        assertThrows(PostException.class, () -> {
            postService.getResponseDto(postId);
        });
        verify(postValidator).getOrThrow(postId);
    }
    /**
     * 게시글 단일 조회 성공 테스트 - 조회수 증가 확인
     */
    @Test
    @DisplayName("게시글 단일 조회 성공 테스트 - 조회수 증가 확인")
    void getPostAndIncrementViewCountSuccess(){
        // given
        Long postId = 1L;
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Test Title", "Test Content"); // Mock 게시글 객체 생성
        // Mock 동작 정의: postRepository에서 해당 ID의 게시글 반환
        given(postValidator.getOrThrow(postId)).willReturn(mockPost);
        // when
        PostResponseDto responseDto = postService.getResponseDto(postId);
        // then
        assertNotNull(responseDto); // 응답 DTO가 null이 아닌지 확인
        assertEquals(mockPost.getTitle(), responseDto.getTitle()); // 제목 확인
        assertEquals(mockPost.getContent(), responseDto.getContent()); // 내용 확인
        assertEquals(1, mockPost.getViewCount()); // 조회수가 1 증가했는지 확인
        verify(postValidator).getOrThrow(postId); // findById 호출 확인
    }
}

