package org.example.restfulblogflatform.service.user;

import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("회원가입 성공 테스트")
    void createUserSuccess() {
        // given
        UserSignUpRequestDto requestDto = new UserSignUpRequestDto(
                "testUser@example.com",
                "testPassword1234!!",
                "testUser"
        );

        // 정적 팩토리 메서드로 User 생성
        User mockUser = User.createUser(
                requestDto.getUsername(),
                "encodedPassword",
                requestDto.getEmail()
        );

        // ID 설정 (Reflection 사용)
        setId(mockUser, 1L);

        // Mock 동작 정의
        given(passwordEncoder.encode(requestDto.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(mockUser);
        doNothing().when(validator).validateEmailNotDuplicate(requestDto.getEmail());

        // when
        Long userId = userService.create(requestDto);

        // then
        assertEquals(1L, userId);

        // 검증
        verify(validator).validateEmailNotDuplicate(requestDto.getEmail());
        verify(passwordEncoder).encode(requestDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("전체 사용자 조회 테스트")
    void readAllUsersSuccess() {
        // given
        User user1 = User.createUser("user1", "password1", "user1@test.com");
        User user2 = User.createUser("user2", "password2", "user2@test.com");

        setId(user1, 1L);
        setId(user2, 2L);

        List<User> mockUsers = Arrays.asList(user1, user2);

        // Mock 동작 정의
        given(userRepository.findAll()).willReturn(mockUsers);

        // when
        List<User> results = userService.getAll();

        // then
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("사용자 삭제 성공 테스트")
    void deleteUserSuccess() {
        // given
        Long userId = 1L;

        // Mock 동작 정의
        doNothing().when(validator).validateUserExists(userId);

        // when
        userService.delete(userId);

        // then
        verify(validator).validateUserExists(userId);
        verify(userRepository).deleteById(userId);
    }

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

