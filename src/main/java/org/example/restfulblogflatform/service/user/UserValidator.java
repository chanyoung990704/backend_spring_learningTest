package org.example.restfulblogflatform.service.user;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.exception.ErrorCode;
import org.example.restfulblogflatform.exception.user.UserException;
import org.example.restfulblogflatform.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void validateEmailNotDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }
}
