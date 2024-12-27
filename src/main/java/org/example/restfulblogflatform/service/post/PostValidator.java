package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.PostException;
import org.example.restfulblogflatform.repository.PostRepository;
import org.springframework.stereotype.Component;

/**
 * 게시글 관련 검증 로직을 처리하는 Validator 클래스
 *
 * @Component: Spring의 Bean으로 등록되어 의존성 주입 가능
 * @RequiredArgsConstructor: final 필드를 포함한 생성자 자동 생성
 */
@Component
@RequiredArgsConstructor
public class PostValidator {

    private final PostRepository postRepository; // 게시글 데이터베이스 접근 객체

    /**
     * 특정 게시글이 존재하는지 검증합니다.
     *
     * @param postId 검증할 게시글의 고유 ID
     * @throws PostException POST_NOT_FOUND 예외를 던집니다. (게시글이 존재하지 않을 경우)
     */
    public void validatePostExists(Long postId) {
        if (!postRepository.existsById(postId)) {
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
