package org.example.restfulblogflatform.service.user;

import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;

import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 처리하기 위한 서비스 인터페이스
 */
public interface UserService {

    /**
     * 새로운 사용자를 생성합니다.
     *
     * @param request 사용자 회원가입 요청 DTO
     * @return 생성된 사용자의 고유 ID
     */
    Long add(UserSignUpRequestDto request);

    /**
     * 특정 사용자를 조회합니다.
     *
     * @param userId 조회할 사용자의 고유 ID
     * @return 조회된 사용자 엔티티
     */
    User get(Long userId);

    /**
     * 모든 사용자를 조회합니다.
     *
     * @return 모든 사용자 엔티티 리스트
     */
    List<User> getAll();

    /**
     * 특정 사용자를 삭제합니다.
     *
     * @param userId 삭제할 사용자의 고유 ID
     */
    void delete(Long userId);
}

