package org.example.restfulblogflatform.service.validator;

import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.UserException;
import org.example.restfulblogflatform.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * 사용자 도메인의 유효성 검증을 담당하는 Validator 클래스
 * BaseValidator를 상속받아 사용자 관련 기본 검증 로직을 구현합니다.
 */
@Component
public class UserValidator extends BaseValidator<User, Long, UserRepository> {

    /**
     * UserRepository를 주입받는 생성자
     * 부모 클래스(BaseValidator)에 repository를 전달합니다.
     *
     * @param repository 사용자 저장소 인터페이스
     */
    public UserValidator(UserRepository repository) {
        super(repository);
    }

    /**
     * 사용자를 찾을 수 없을 때 발생시킬 예외를 정의합니다.
     * BaseValidator의 추상 메서드를 구현합니다.
     *
     * @return UserException 사용자 관련 사용자 정의 예외
     */
    @Override
    protected RuntimeException getNotFoundException() {
        return new UserException(ErrorCode.USER_NOT_FOUND);
    }

    /**
     * 이메일 중복 여부를 검증하는 도메인 특화 메서드
     *
     * @param email 검증할 이메일 주소
     * @throws UserException 이메일이 이미 존재할 경우 발생하는 예외
     */
    public void validateEmailNotDuplicate(String email) {
        if (repository.existsByEmail(email)) {
            throw new UserException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}