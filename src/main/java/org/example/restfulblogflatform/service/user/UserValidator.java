package org.example.restfulblogflatform.service.user;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.UserException;
import org.example.restfulblogflatform.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * 사용자 관련 검증 로직을 처리하는 Validator 클래스
 *
 * @Component: Spring의 Bean으로 등록되어 의존성 주입 가능
 * @RequiredArgsConstructor: final 필드를 포함한 생성자 자동 생성
 */
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository; // 사용자 데이터베이스 접근 객체

    /**
     * 사용자가 존재하는지 검증합니다.
     *
     * @param userId 검증할 사용자의 고유 ID
     * @throws UserException USER_NOT_FOUND 예외를 던집니다. (사용자가 존재하지 않을 경우)
     */
    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserException(ErrorCode.USER_NOT_FOUND); // 사용자 미존재 예외 처리
        }
    }

    /**
     * 이메일이 중복되지 않았는지 검증합니다.
     *
     * @param email 검증할 이메일 주소
     * @throws UserException DUPLICATE_EMAIL 예외를 던집니다. (이메일이 이미 존재할 경우)
     */
    public void validateEmailNotDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(ErrorCode.DUPLICATE_EMAIL); // 이메일 중복 예외 처리
        }
    }

    /**
     * 특정 사용자를 조회하고, 존재하지 않을 경우 예외를 던집니다.
     *
     * @param userId 조회할 사용자의 고유 ID
     * @return 조회된 사용자 엔티티
     * @throws UserException USER_NOT_FOUND 예외를 던집니다. (사용자가 존재하지 않을 경우)
     */
    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND)); // 사용자 미존재 예외 처리
    }
}

