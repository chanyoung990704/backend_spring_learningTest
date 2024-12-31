package org.example.restfulblogflatform.service.post;

import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.PostException;
import org.example.restfulblogflatform.exception.business.UserException;
import org.example.restfulblogflatform.repository.PostRepository;
import org.example.restfulblogflatform.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostValidator postValidator;

    @InjectMocks
    private PostServiceImpl postService;

    /**
     * 게시글 생성 성공 테스트
     */
    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void addSuccess() {
        // given: 정상적인 게시글 생성 요청
        Long userId = 1L;
        PostRequest postRequest = new PostRequest("Test Title", "Test Content");
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, postRequest.getTitle(), postRequest.getContent());

        // Mock 동작 정의: 사용자 조회 및 게시글 저장
        given(userService.get(userId)).willReturn(mockUser);
        given(postRepository.save(any(Post.class))).willReturn(mockPost);

        // when: 게시글 생성 서비스 호출
        PostResponse response = postService.add(postRequest, userId);

        // then: 응답 검증 및 Mock 객체 동작 확인
        assertNotNull(response);
        assertEquals(postRequest.getTitle(), response.getTitle());
        assertEquals(postRequest.getContent(), response.getContent());
        verify(userService).get(userId);
        verify(postRepository).save(any(Post.class));
    }

    /**
     * 게시글 생성 실패 테스트 - 사용자 없음
     */
    @Test
    @DisplayName("게시글 생성 실패 테스트 - 사용자 없음")
    void addFailDueToUserNotFound() {
        // given: 존재하지 않는 사용자 ID로 요청 생성
        Long userId = 1L;
        PostRequest postRequest = new PostRequest("Test Title", "Test Content");

        // Mock 동작 정의: 사용자 조회 시 예외 발생
        given(userService.get(userId)).willThrow(new UserException(ErrorCode.USER_NOT_FOUND));

        // when & then: 예외 발생 여부 확인 및 검증
        UserException exception = assertThrows(UserException.class, () -> postService.add(postRequest, userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(userService).get(userId);
        verify(postRepository, never()).save(any(Post.class));
    }

    /**
     * 게시글 단일 조회 성공 테스트
     */
    @Test
    @DisplayName("게시글 단일 조회 성공 테스트")
    void getSuccess() {
        // given: 정상적인 게시글 ID로 요청 생성
        Long postId = 1L;
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Test Title", "Test Content");

        // Mock 동작 정의: 게시글 조회 성공
        given(postValidator.getPostOrThrow(postId)).willReturn(mockPost);

        // when: 게시글 조회 서비스 호출
        Post result = postService.get(postId);

        // then: 결과 검증 및 Mock 객체 동작 확인
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(postValidator).getPostOrThrow(postId);
    }

    /**
     * 게시글 단일 조회 실패 테스트 - 게시글 없음
     */
    @Test
    @DisplayName("게시글 단일 조회 실패 테스트 - 게시글 없음")
    void getFailDueToPostNotFound() {
        // given: 존재하지 않는 게시글 ID로 요청 생성
        Long postId = 1L;

        // Mock 동작 정의: 게시글 조회 시 예외 발생
        given(postValidator.getPostOrThrow(postId)).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

        // when & then: 예외 발생 여부 확인 및 검증
        PostException exception = assertThrows(PostException.class, () -> postService.get(postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());

        verify(postValidator).getPostOrThrow(postId);
    }

    /**
     * 모든 게시글 조회 성공 테스트
     */
    @Test
    @DisplayName("모든 게시글 조회 성공 테스트")
    void getAllSuccess() {
        // given: 여러 개의 Mock 게시글 데이터 생성
        User mockUser1 = User.createUser("user1", "password1", "user1@example.com");
        User mockUser2 = User.createUser("user2", "password2", "user2@example.com");

        List<Post> mockPosts = Arrays.asList(
                Post.createPost(mockUser1, "Title 1", "Content 1"),
                Post.createPost(mockUser2, "Title 2", "Content 2")
        );

        // Mock 동작 정의: 모든 게시글 조회 성공
        given(postRepository.findAll()).willReturn(mockPosts);

        // when: 모든 게시글 조회 서비스 호출
        List<Post> results = postService.getAll();

        // then: 결과 검증 및 Mock 객체 동작 확인
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(postRepository).findAll();
    }

    /**
     * 모든 게시글 조회 실패 테스트 - 데이터 없음
     */
    @Test
    @DisplayName("모든 게시글 조회 실패 테스트 - 데이터 없음")
    void getAllFailDueToNoPosts() {
        // given: 빈 리스트 반환 설정
        given(postRepository.findAll()).willReturn(Collections.emptyList());

        // when: 모든 게시글 조회 서비스 호출
        List<Post> results = postService.getAll();

        // then: 결과가 비어 있는지 확인 (예외 대신 빈 리스트 반환)
        assertNotNull(results);
        assertTrue(results.isEmpty());

        verify(postRepository).findAll();
    }

    /**
     * 게시글 업데이트 성공 테스트
     */
    @Test
    @DisplayName("게시글 업데이트 성공 테스트")
    void updateSuccess() {
        // given: 정상적인 업데이트 요청 데이터 생성
        Long postId = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";

        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Original Title", "Original Content");

        // Mock 동작 정의: 기존 게시글 조회 성공 처리
        given(postValidator.getPostOrThrow(postId)).willReturn(mockPost);

        // when: 업데이트 서비스 호출
        PostResponse response = postService.update(postId, updatedTitle, updatedContent);

        // then: 응답 검증 및 Mock 객체 동작 확인
        assertNotNull(response);
        assertEquals(updatedTitle, response.getTitle());
        assertEquals(updatedContent, response.getContent());

        verify(postValidator).getPostOrThrow(postId);
    }

    /**
     * 삭제 실패 처리
     */
}
