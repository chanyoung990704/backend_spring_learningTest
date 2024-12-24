package org.example.restfulblogflatform.service.user;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator validator;


    @Transactional
    @Override
    public Long create(UserSignUpRequestDto request) {
        validator.validateEmailNotDuplicate(request.getEmail());

        User user = request.toEntity(passwordEncoder);

        return userRepository.save(user).getId();
    }

    @Override
    public User read(Long userId) {
        return validator.getUserOrThrow(userId);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }


    @Transactional
    @Override
    public void delete(Long userId) {
        validator.validateUserExists(userId);
        userRepository.deleteById(userId);
    }

//    @Transactional
//    @Override
//    public void update(Long userId, UserUpdateDto dto) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        user.update(dto);
//    }


}
