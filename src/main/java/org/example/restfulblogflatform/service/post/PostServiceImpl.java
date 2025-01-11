package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.post.request.PostRequestDto;
import org.example.restfulblogflatform.dto.post.response.PostResponseDto;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.PostRepository;
import org.example.restfulblogflatform.service.user.UserService;
import org.example.restfulblogflatform.service.validator.PostValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시글(Post) 관련 비즈니스 로직을 처리하는 서비스 구현체.
 *
 * 이 클래스는 게시글 생성, 조회, 수정, 삭제와 같은 기능을 제공합니다.
 * 데이터베이스와 상호작용하며, 데이터 검증 및 연관된 서비스 호출을 통해 비즈니스 로직을 처리합니다.
 */
@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
@RequiredArgsConstructor // Lombok을 사용하여 final 필드의 생성자를 자동 생성
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository; // 게시글 데이터 처리 JPA Repository
    private final UserService userService; // 사용자 관련 비즈니스 로직 처리
    private final PostValidator postValidator; // 게시글 검증 로직 처리 클래스

    /**
     * 게시글 생성
     *
     * 사용자가 새로운 게시글을 작성합니다. 사용자 정보를 확인한 뒤, 제목과 내용을 기반으로
     * 게시글을 저장하고, 저장된 게시글 정보를 DTO로 반환합니다.
     *
     * @param postRequestDto 게시글 생성 요청 DTO (제목과 내용 포함)
     * @param userId         게시글 작성자의 사용자 ID
     * @return 생성된 게시글의 정보를 담은 응답 DTO
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public PostResponseDto add(PostRequestDto postRequestDto, Long userId) {
        User user = userService.get(userId); // 사용자 정보 가져오기
        Post post = Post.createPost(user, postRequestDto.getTitle(), postRequestDto.getContent()); // 게시글 생성
        return PostResponseDto.of(postRepository.save(post)); // 저장 후 DTO로 반환
    }

    /**
     * 게시글 단일 조회
     *
     * 특정 ID를 가진 게시글을 조회합니다. 게시글이 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 엔티티
     */
    @Override
    public Post get(Long postId) {
        return postValidator.getOrThrow(postId); // 게시글 검증 후 반환
    }

    /**
     * 게시글 단일 조회 (응답 DTO 형태)
     *
     * 특정 ID를 가진 게시글을 조회하고, 조회수를 증가시킨 후 해당 데이터를 응답 DTO 형태로 반환합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 정보를 담은 응답 DTO
     */
    @Override
    @Transactional
    public PostResponseDto getResponseDto(Long postId) {
        // 게시글을 데이터베이스에서 가져옴( validator 사용)
        Post post = postValidator.getOrThrow(postId);
        // 조회수 증가
        post.incrementViewCount();
        // 엔티티를 DTO로 변환하여 반환
        return PostResponseDto.of(post);
    }

    /**
     * 모든 게시글 조회 (페이징 처리)
     *
     * 모든 게시글 데이터를 페이징 처리하여 조회합니다. 클라이언트가 페이징 조건(페이지 크기, 정렬 등)을 지정할 수 있으며,
     * 페이징 메타데이터와 게시글 리스트를 응답합니다.
     *
     * @param pageable 페이징 요청 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return 페이징 처리된 게시글 리스트 (DTO 형태)
     */
    @Override
    public Page<PostResponseDto> getAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostResponseDto::of); // Post -> PostResponseDto 변환
    }

    /**
     * 게시글 수정
     *
     * 특정 게시글의 제목과 내용을 수정합니다. 수정하려는 게시글이 존재하지 않으면 예외를 발생시킵니다.
     * 제목과 내용을 업데이트한 후 업데이트된 게시글 정보를 DTO로 반환합니다.
     *
     * @param postId  수정할 게시글의 ID
     * @param title   새로운 제목
     * @param content 새로운 내용
     * @return 수정된 게시글 정보를 담은 응답 DTO
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public PostResponseDto update(Long postId, String title, String content) {
        Post post = postValidator.getOrThrow(postId); // 게시글 가져오기 및 검증
        post.update(title, content); // 제목과 내용 업데이트
        return PostResponseDto.of(post); // DTO로 반환
    }

    /**
     * 게시글 삭제
     *
     * 특정 게시글을 삭제합니다. 삭제하려는 게시글이 존재하지 않거나 삭제 조건을 만족하지 않으면 예외를 발생시킵니다.
     * 삭제 처리 시 연관 관계를 해제합니다.
     *
     * @param postId 삭제할 게시글의 ID
     */
    @Override
    @Transactional // 쓰기 작업이므로 읽기 전용 트랜잭션 해제
    public void delete(Long postId) {
        Post post = postValidator.getOrThrow(postId); // 게시글 가져오기 및 검증
        post.getUser().removePost(post); // 사용자-게시글 연관 관계 해제
    }
}

