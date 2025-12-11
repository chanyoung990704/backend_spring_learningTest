package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.post.request.PostRequestDto;
import org.example.restfulblogflatform.dto.post.response.PostResponseDto;
import org.example.restfulblogflatform.entity.FileAttachment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.PostRepository;
import org.example.restfulblogflatform.service.file.FileStorageService;
import org.example.restfulblogflatform.service.user.UserService;
import org.example.restfulblogflatform.service.validator.PostValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 게시글(Post) 관련 비즈니스 로직을 처리하는 서비스 구현체.
 *
 * 이 클래스는 게시글 생성, 조회, 수정, 삭제와 같은 기능을 제공합니다.
 * 데이터베이스와 상호작용하며, 데이터 검증 및 연관된 서비스 호출을 통해 비즈니스 로직을 처리합니다.
 */
@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostValidator postValidator;
    private final FileStorageService fileStorageService;      // 파일 저장소(로컬/클라우드) 관련 서비스

    /**
     * 게시글 생성 (파일 업로드 처리 포함)
     */
    @Override
    @Transactional
    public PostResponseDto add(PostRequestDto postRequestDto, Long userId) {
        // 1) 작성자 가져오기
        User user = userService.get(userId);
        // 2) Post 엔티티 생성
        Post post = Post.createPost(user, postRequestDto.getTitle(), postRequestDto.getContent());
        // 3) 파일 업로드 처리
        if (postRequestDto.getFiles() != null && !postRequestDto.getFiles().isEmpty()) {
            handleFileUploads(postRequestDto.getFiles(), post);
        }
        // 4) DB 저장 후, DTO 변환
        return PostResponseDto.of(postRepository.save(post));
    }

    /**
     * 게시글 단일 조회 (엔티티)
     */
    @Override
    public Post get(Long postId) {
        return postValidator.getOrThrow(postId);
    }

    /**
     * 게시글 단일 조회 (응답 DTO + 조회수 증가)
     */
    @Override
    @Transactional
    public PostResponseDto getResponseDto(Long postId) {
        Post post = postValidator.getOrThrow(postId);
        post.incrementViewCount();
        return PostResponseDto.of(post);
    }

    /**
     * 모든 게시글 조회 (페이징, DTO 변환)
     */
    @Override
    public Page<PostResponseDto> getAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostResponseDto::of);
    }

    /**
     * 게시글 수정
     */
    @Override
    @Transactional
    public PostResponseDto update(Long postId, String title, String content) {
        Post post = postValidator.getOrThrow(postId);
        post.update(title, content);
        return PostResponseDto.of(post);
    }

    /**
     * 게시글 삭제
     */
    @Override
    @Transactional
    public void delete(Long postId) {
        Post post = postValidator.getOrThrow(postId);
        post.getUser().removePost(post); // 연관관계 해제
        postRepository.delete(post);
        // 필요한 경우, 첨부파일도 함께 제거(물리 파일 삭제) 로직을 구현해야 합니다.
    }

    /**
     * 파일 업로드 처리
     */
    protected void handleFileUploads(List<MultipartFile> files, Post post) {
        files.forEach(file -> {
            try {
                // 1) 원본 파일명
                String originalFileName = file.getOriginalFilename();
                // 2) 실제 저장 파일명 (UUID 등으로 구분)
                String storedFileName = fileStorageService.storeFile(file);
                // 3) 저장 위치(혹은 접근 가능한 URL) 조회
                String filePath = fileStorageService.getFilePath(storedFileName);

                // 4) FileAttachment 엔티티 생성
                FileAttachment attachment = FileAttachment.createFileAttachment(
                        originalFileName,
                        storedFileName,
                        filePath,
                        file.getSize(),
                        file.getContentType(),
                        post
                );

                // 5) Post 엔티티에 첨부파일 추가 (양방향 연관관계)
                post.addAttachment(attachment);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

