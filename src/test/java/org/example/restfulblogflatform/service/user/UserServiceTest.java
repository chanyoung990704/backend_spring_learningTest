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
    @DisplayName("회원가입성공테스트")
    void createUserSuccess() {
        // given
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .username("testUser")
                .password("testPassword1234!!")
                .email("testUser@example.com")
                .build();

        User mockUser = User.builder()
                .id(1L)
                .username(requestDto.getUsername())
                .password("encodedPassword")
                .email(requestDto.getEmail())
                .build();

        // when
        given(passwordEncoder.encode(any())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(mockUser);
        doNothing().when(validator).validateEmailNotDuplicate(any());

        // then
        Long userId = userService.create(requestDto);
        assertEquals(userId, mockUser.getId());

        verify(validator).validateEmailNotDuplicate(requestDto.getEmail());
        verify(passwordEncoder).encode(requestDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("전체 사용자 조회 테스트")
    void readAllUsersSuccess() {
        // given
        List<User> mockUsers = Arrays.asList(

                User.builder()
                        .id(1L)
                        .username("user1")
                        .password("password1")
                        .email("user1@test.com")
                        .build(),

                User.builder()
                        .id(2L)
                        .username("user2")
                        .password("password2")
                        .email("user2@test.com")
                        .build()

        );

        // when
        given(userRepository.findAll()).willReturn(mockUsers);
        List<User> results = userService.readAll();

        // then
        assertEquals(results.size(), 2);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("사용자 삭제 성공 테스트")
    void deleteUserSuccess() {
        // given
        Long userId = 1L;

        // when
        doNothing().when(validator).validateUserExists(userId);
        doNothing().when(userRepository).deleteById(userId);
        userService.delete(userId);

        // then
        verify(validator).validateUserExists(userId);
        verify(userRepository).deleteById(userId);
    }
}
