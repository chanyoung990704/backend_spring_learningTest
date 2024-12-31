package org.example.restfulblogflatform.service;

import org.example.restfulblogflatform.ResTfulBlogFlatformApplication;
import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.CommentException;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.example.restfulblogflatform.repository.PostRepository;
import org.example.restfulblogflatform.repository.UserRepository;
import org.example.restfulblogflatform.service.comment.CommentService;
import org.example.restfulblogflatform.service.post.PostService;
import org.example.restfulblogflatform.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ResTfulBlogFlatformApplication.class) // 애플리케이션 컨텍스트 로드
@ActiveProfiles("test") // 테스트 프로파일 활성화
@Transactional // 각 테스트 후 데이터 롤백 보장
class IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 사용자 생성 -> 게시글 생성 -> 댓글 생성 및 조회 통합 테스트
     */
    @Test
    @DisplayName("사용자 생성 -> 게시글 생성 -> 댓글 생성 및 조회 통합 테스트")
    void fullIntegrationTest() {
        // 사용자 생성
        Long userId = userService.add(new UserSignUpRequestDto("testUser@example.com", "password123!", "testUser"));
        assertNotNull(userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        assertEquals("testUser@example.com", user.getEmail());

        // 게시글 생성
        PostRequest postRequest = new PostRequest("Test Post Title", "Test Post Content");
        PostResponse postResponse = postService.add(postRequest, userId);
        assertNotNull(postResponse);
        assertEquals("Test Post Title", postResponse.getTitle());

        // 댓글 생성 및 조회 확인
        Comment comment = commentService.add(postResponse.getId(), userId, "This is a test comment");
        assertNotNull(comment);
        List<CommentResponseDto> comments = commentService.getAll(postResponse.getId());
        assertEquals(1, comments.size());
        assertEquals("This is a test comment", comments.get(0).getContent());

    }

}