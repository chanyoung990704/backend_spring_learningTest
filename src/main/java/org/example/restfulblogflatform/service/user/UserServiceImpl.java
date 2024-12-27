package org.example.restfulblogflatform.service.user;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 구현체
 *
 * @Service: Spring의 Service 계층으로 등록
 * @Transactional: 읽기 전용 트랜잭션 기본 설정
 * @RequiredArgsConstructor: final 필드를 포함한 생성자 자동 생성
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // 사용자 데이터베이스 접근 객체
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 도구
    private final UserValidator validator; // 사용자 검증 로직을 처리하는 Validator

    /**
     * 새로운 사용자를 생성합니다.
     *
     * @param request 사용자 회원가입 요청 DTO
     * @return 생성된 사용자의 고유 ID
     */
    @Transactional // 데이터 변경이 발생하므로 별도로 읽기 전용 트랜잭션 해제
    @Override
    public Long create(UserSignUpRequestDto request) {
        // 이메일 중복 여부 검증
        validator.validateEmailNotDuplicate(request.getEmail());

        // 정적 팩토리 메서드를 사용해 User 객체 생성 (객체 생성 책임 분리)
        User user = User.createUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()), // 비밀번호 암호화 처리
                request.getEmail()
        );

        // 사용자 저장 후 ID 반환
        return userRepository.save(user).getId();
    }

    /**
     * 특정 사용자를 조회합니다.
     *
     * @param userId 조회할 사용자의 고유 ID
     * @return 조회된 사용자 엔티티
     */
    @Override
    public User get(Long userId) {
        // Validator를 통해 존재 여부 검증 및 사용자 반환 (예외 처리 포함)
        return validator.getUserOrThrow(userId);
    }

    /**
     * 모든 사용자를 조회합니다.
     *
     * @return 모든 사용자 엔티티 리스트
     */
    @Override
    public List<User> getAll() {
        // 모든 사용자 데이터를 데이터베이스에서 조회하여 반환
        return userRepository.findAll();
    }

    /**
     * 특정 사용자를 삭제합니다.
     *
     * @param userId 삭제할 사용자의 고유 ID
     */
    @Transactional // 데이터 변경이 발생하므로 별도로 읽기 전용 트랜잭션 해제
    @Override
    public void delete(Long userId) {
        // 삭제 전 사용자 존재 여부 검증 (예외 발생 가능)
        validator.validateUserExists(userId);

        // 사용자 데이터베이스에서 삭제 수행
        userRepository.deleteById(userId);
    }
}
