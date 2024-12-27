package org.example.restfulblogflatform.service.post;

import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.PostRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

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
    void createPostSuccess() {
        // given
        Long userId = 1L;
        PostRequest postRequest = new PostRequest("Test Title", "Test Content");
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, postRequest.getTitle(), postRequest.getContent());

        // Mock 동작 정의
        given(userService.get(userId)).willReturn(mockUser);
        given(postRepository.save(any(Post.class))).willReturn(mockPost);

        // when
        PostResponse response = postService.createPost(postRequest, userId);

        // then
        assertNotNull(response);
        assertEquals(postRequest.getTitle(), response.getTitle());
        assertEquals(postRequest.getContent(), response.getContent());
        verify(userService).get(userId);
        verify(postRepository).save(any(Post.class));
    }

    /**
     * 게시글 단일 조회 성공 테스트
     */
    @Test
    @DisplayName("게시글 단일 조회 성공 테스트")
    void getPostSuccess() {
        // given
        Long postId = 1L;
        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Test Title", "Test Content");

        // Mock 동작 정의
        given(postValidator.getPostOrThrow(postId)).willReturn(mockPost);

        // when
        Post result = postService.getPost(postId);

        // then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(postValidator).getPostOrThrow(postId);
    }

    /**
     * 모든 게시글 조회 성공 테스트
     */
    @Test
    @DisplayName("모든 게시글 조회 성공 테스트")
    void getAllPostsSuccess() {
        // given
        User mockUser1 = User.createUser("user1", "password1", "user1@example.com");
        User mockUser2 = User.createUser("user2", "password2", "user2@example.com");

        List<Post> mockPosts = Arrays.asList(
                Post.createPost(mockUser1, "Title 1", "Content 1"),
                Post.createPost(mockUser2, "Title 2", "Content 2")
        );

        // Mock 동작 정의
        given(postRepository.findAll()).willReturn(mockPosts);

        // when
        List<Post> results = postService.getAllPosts();

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(postRepository).findAll();
    }

    /**
     * 게시글 업데이트 성공 테스트
     */
    @Test
    @DisplayName("게시글 업데이트 성공 테스트")
    void updatePostSuccess() {
        // given
        Long postId = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";

        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Original Title", "Original Content");

        // Mock 동작 정의
        given(postValidator.getPostOrThrow(postId)).willReturn(mockPost);

        // when
        PostResponse response = postService.updatePost(postId, updatedTitle, updatedContent);

        // then
        assertNotNull(response);
        assertEquals(updatedTitle, response.getTitle());
        assertEquals(updatedContent, response.getContent());

        verify(postValidator).getPostOrThrow(postId);
    }

    /**
     * 게시글 삭제 성공 테스트
     */
    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void deletePostSuccess() {
        // given
        Long postId = 1L;

        User mockUser = User.createUser("testUser", "password", "test@example.com");
        Post mockPost = Post.createPost(mockUser, "Test Title", "Test Content");

        // Mock 동작 정의
        given(postValidator.getPostOrThrow(postId)).willReturn(mockPost);

        // when
        postService.deletePost(postId);

        // then
        verify(postValidator).getPostOrThrow(postId);
        verify(postRepository).delete(mockPost);
    }
}


