package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.PostRepository;
import org.example.restfulblogflatform.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글(Post) 관련 비즈니스 로직을 처리하는 서비스 구현체.
 * 게시글 생성, 조회, 수정, 삭제와 같은 주요 기능을 제공합니다.
 */
@Service // Spring의 Service 계층으로 등록
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository; // 게시글 데이터 처리 JPA Repository
    private final UserService userService; // 사용자 관련 비즈니스 로직 처리
    private final PostValidator postValidator; // 게시글 검증 로직 처리 클래스

    /**
     * 게시글을 생성합니다.
     *
     * @param postRequest 게시글 생성 요청 DTO (제목, 내용 등 포함)
     * @param userId 게시글 작성자의 사용자 ID
     * @return 생성된 게시글의 응답 DTO
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public PostResponse add(PostRequest postRequest, Long userId) {
        // 사용자 정보 가져오기
        User user = userService.get(userId);

        // 게시글 생성 및 연관 관계 설정
        Post post = Post.createPost(user, postRequest.getTitle(), postRequest.getContent());

        // 저장 후 DTO 변환하여 반환
        return PostResponse.of(postRepository.save(post));
    }

    /**
     * 특정 게시글을 조회합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 엔티티
     */
    @Override
    public Post get(Long postId) {
        // 게시글 가져오기 및 검증 포함
        return postValidator.getPostOrThrow(postId);
    }

    /**
     * 모든 게시글을 조회합니다.
     *
     * @return 모든 게시글 엔티티 리스트
     */
    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    /**
     * 특정 게시글을 업데이트합니다.
     *
     * @param postId 업데이트할 게시글의 ID
     * @param title 새로운 제목
     * @param content 새로운 내용
     * @return 업데이트된 게시글의 응답 DTO
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public PostResponse update(Long postId, String title, String content) {
        // 게시글 가져오기 및 검증 수행
        Post post = postValidator.getPostOrThrow(postId);

        // 업데이트 수행 (제목과 내용을 변경)
        post.update(title, content);

        // 업데이트된 엔티티를 DTO로 변환하여 반환
        return PostResponse.of(post);
    }

    /**
     * 특정 게시글을 삭제합니다.
     *
     * @param postId 삭제할 게시글의 ID
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public void delete(Long postId) {
        // 게시글 가져오기 및 검증 수행
        Post post = postValidator.getPostOrThrow(postId);

        // 연관 관계 해제 (사용자 -> 게시글)
        post.getUser().removePost(post);
    }
}
