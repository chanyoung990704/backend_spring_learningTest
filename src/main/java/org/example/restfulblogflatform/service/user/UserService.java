package org.example.restfulblogflatform.service.user;

import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;

import java.util.List;

public interface UserService {

    Long create(UserSignUpRequestDto request); // Create

    User read(Long userId); // Read (단일 조회)

    List<User> readAll(); // Read (전체 조회)

    void delete(Long userId); // Delete

}
