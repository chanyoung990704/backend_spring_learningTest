package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.PostException;
import org.example.restfulblogflatform.repository.PostRepository;
import org.springframework.stereotype.Component;

/**
 * 게시글(Post) 관련 검증 로직을 처리하는 Validator 클래스.
 * 게시글의 존재 여부를 확인하거나, 조회 시 유효성을 검증하는 역할을 수행합니다.
 *
 * @Component: Spring의 Bean으로 등록되어 다른 클래스에서 의존성 주입을 통해 사용 가능.
 * @RequiredArgsConstructor: final 필드를 포함한 생성자를 자동으로 생성하여 의존성 주입 지원.
 */
@Component // Spring의 Bean으로 등록
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class PostValidator {

    private final PostRepository postRepository; // 게시글 데이터베이스 접근 객체

    /**
     * 특정 게시글이 존재하는지 검증합니다.
     *
     * @param postId 검증할 게시글의 고유 ID
     * @throws PostException POST_NOT_FOUND 예외를 던집니다. (게시글이 존재하지 않을 경우)
     */
    public void validatePostExists(Long postId) {
        if (!postRepository.existsById(postId)) { // 게시글 존재 여부 확인
            throw new PostException(ErrorCode.POST_NOT_FOUND); // 게시글 미존재 예외 처리
        }
    }

    /**
     * 특정 게시글을 조회하고, 존재하지 않을 경우 예외를 던집니다.
     *
     * @param postId 조회할 게시글의 고유 ID
     * @return 조회된 게시글 엔티티
     * @throws PostException POST_NOT_FOUND 예외를 던집니다. (게시글이 존재하지 않을 경우)
     */
    public Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND)); // 게시글 미존재 예외 처리
    }
}
