package org.example.restfulblogflatform.service.user;

import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.business.UserException;
import org.example.restfulblogflatform.repository.UserRepository;
import org.example.restfulblogflatform.service.validator.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserValidator validator;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * 회원가입 성공 테스트
     */
    @Test
    @DisplayName("회원가입 성공 테스트")
    void createUserSuccess() {
        // given: 테스트에 필요한 데이터 및 Mock 동작 정의
        UserSignUpRequestDto requestDto = new UserSignUpRequestDto(
                "testUser@example.com",
                "testPassword1234!!",
                "testUser"
        );

        // 정적 팩토리 메서드로 User 객체 생성
        User mockUser = User.createUser(
                requestDto.getUsername(),
                "encodedPassword",
                requestDto.getEmail()
        );

        // Reflection을 사용해 ID 설정 (private 필드 접근)
        setId(mockUser, 1L);

        // Mock 동작 정의: 비밀번호 인코딩 및 저장 로직 설정
        given(passwordEncoder.encode(requestDto.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(mockUser);
        doNothing().when(validator).validateEmailNotDuplicate(requestDto.getEmail());

        // when: 서비스 호출
        Long userId = userService.add(requestDto);

        // then: 결과 검증
        assertEquals(1L, userId);

        // Mock 객체의 동작 검증
        verify(validator).validateEmailNotDuplicate(requestDto.getEmail());
        verify(passwordEncoder).encode(requestDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    /**
     * 이메일 중복으로 인한 회원가입 실패 테스트
     */
    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 중복")
    void createUserFailDueToDuplicateEmail() {
        // given: 중복된 이메일로 요청 생성
        UserSignUpRequestDto requestDto = new UserSignUpRequestDto(
                "duplicateEmail@example.com",
                "testPassword1234!!",
                "testUser"
        );

        // Mock 동작 정의: 이메일 중복 검증 시 예외 발생
        doThrow(new UserException(ErrorCode.DUPLICATE_EMAIL))
                .when(validator).validateEmailNotDuplicate(requestDto.getEmail());

        // when & then: 예외 발생 여부 확인 및 검증
        UserException exception = assertThrows(UserException.class, () -> userService.add(requestDto));
        assertEquals(ErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());

        // Mock 객체의 동작 검증 (비밀번호 인코딩 및 저장은 호출되지 않음)
        verify(validator).validateEmailNotDuplicate(requestDto.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * 전체 사용자 조회 성공 테스트
     */
    @Test
    @DisplayName("전체 사용자 조회 테스트")
    void readAllUsersSuccess() {
        // given: Mock 데이터 생성 (2명의 사용자)
        User user1 = User.createUser("user1", "password1", "user1@test.com");
        User user2 = User.createUser("user2", "password2", "user2@test.com");

        setId(user1, 1L);
        setId(user2, 2L);

        List<User> mockUsers = Arrays.asList(user1, user2);

        // Mock 동작 정의: findAll 호출 시 사용자 리스트 반환
        given(userRepository.findAll()).willReturn(mockUsers);

        // when: 서비스 호출
        List<User> results = userService.getAll();

        // then: 결과 검증 (사용자 수와 ID 확인)
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());

        // Mock 객체의 동작 검증
        verify(userRepository).findAll();
    }

    /**
     * 전체 사용자 조회 실패 테스트 - 데이터 없음
     */
    @Test
    @DisplayName("전체 사용자 조회 실패 테스트 - 데이터 없음")
    void readAllUsersFailDueToEmptyData() {
        // given: 빈 리스트 반환 설정
        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // when: 서비스 호출
        List<User> results = userService.getAll();

        // then: 결과가 비어 있는지 확인
        assertTrue(results.isEmpty());

        // Mock 객체의 동작 검증
        verify(userRepository).findAll();
    }

    /**
     * 사용자 삭제 성공 테스트
     */
    @Test
    @DisplayName("사용자 삭제 성공 테스트")
    void deleteUserSuccess() {
        // given: 삭제할 사용자 ID 설정 및 Mock 동작 정의
        Long userId = 1L;

        doNothing().when(validator).validateExists(userId);

        // when: 서비스 호출 (삭제)
        userService.delete(userId);

        // then: 검증 (삭제 로직이 정상적으로 호출되었는지 확인)
        verify(validator).validateExists(userId);
        verify(userRepository).deleteById(userId);
    }

    /**
     * 사용자 삭제 실패 테스트 - 존재하지 않는 사용자
     */
    @Test
    @DisplayName("사용자 삭제 실패 테스트 - 사용자 없음")
    void deleteUserFailDueToNotFound() {
        // given: 존재하지 않는 사용자 ID 설정 및 Mock 동작 정의 (예외 발생)
        Long nonExistentUserId = 999L;
        doThrow(new UserException(ErrorCode.USER_NOT_FOUND))
                .when(validator).validateExists(nonExistentUserId);

        // when & then: 예외 발생 여부 확인 및 검증
        UserException exception = assertThrows(UserException.class, () -> userService.delete(nonExistentUserId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(validator).validateExists(nonExistentUserId);
        verify(userRepository, never()).deleteById(anyLong());
    }

    /**
     * Reflection을 사용하여 private 필드 ID 설정 메서드.
     */
    private void setId(User user, Long id) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

