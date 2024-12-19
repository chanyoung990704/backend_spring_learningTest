package org.example.restfulblogflatform.service;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(UserSignUpRequestDto request) {
        validateDuplicateEmail(request.getEmail());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User saved = userRepository.save(user);

        return saved.getId();
    }


    private void validateDuplicateEmail(String email) {
        if(userRepository.existsByEmail(email))
            throw new DataIntegrityViolationException("이미 사용 중인 이메일입니다. \n" + email);
    }

}
