package org.example.restfulblogflatform.service.validator;

import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.CommentException;
import org.example.restfulblogflatform.repository.CommentRepository;
import org.springframework.stereotype.Component;

/**
 * 댓글 도메인의 유효성 검증을 담당하는 Validator 클래스
 * BaseValidator를 상속받아 댓글 관련 기본 검증 로직을 구현합니다.
 *
 * @Component: Spring Bean으로 등록하여 DI 가능하게 함
 */
@Component
public class CommentValidator extends BaseValidator<Comment, Long, CommentRepository> {

    /**
     * CommentRepository를 주입받는 생성자
     * 부모 클래스(BaseValidator)에 repository를 전달합니다.
     *
     * @param repository 댓글 저장소 인터페이스
     */
    public CommentValidator(CommentRepository repository) {
        super(repository);
    }

    /**
     * 댓글을 찾을 수 없을 때 발생시킬 예외를 정의합니다.
     * BaseValidator의 추상 메서드를 구현합니다.
     *
     * @return CommentException 댓글 관련 사용자 정의 예외
     */
    @Override
    protected RuntimeException getNotFoundException() {
        return new CommentException(ErrorCode.COMMENT_NOT_FOUND);
    }
}