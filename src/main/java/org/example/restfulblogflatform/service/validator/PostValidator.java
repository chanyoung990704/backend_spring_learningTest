package org.example.restfulblogflatform.service.validator;

import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.PostException;
import org.example.restfulblogflatform.repository.PostRepository;
import org.springframework.stereotype.Component;

/**
 * 게시글 도메인의 유효성 검증을 담당하는 Validator 클래스
 * BaseValidator를 상속받아 게시글 관련 기본 검증 로직을 구현합니다.
 */
@Component
public class PostValidator extends BaseValidator<Post, Long, PostRepository> {

    /**
     * PostRepository를 주입받는 생성자
     * 부모 클래스(BaseValidator)에 repository를 전달합니다.
     *
     * @param repository 게시글 저장소 인터페이스
     */
    public PostValidator(PostRepository repository) {
        super(repository);
    }

    /**
     * 게시글을 찾을 수 없을 때 발생시킬 예외를 정의합니다.
     * BaseValidator의 추상 메서드를 구현합니다.
     *
     * @return PostException 게시글 관련 사용자 정의 예외
     */
    @Override
    protected RuntimeException getNotFoundException() {
        return new PostException(ErrorCode.POST_NOT_FOUND);
    }
}