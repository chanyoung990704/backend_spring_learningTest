package org.example.restfulblogflatform.service.user;

import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.entity.User;

import java.util.List;

public interface UserService {

    Long create(UserSignUpRequestDto request); // Create

    User get(Long userId); // Read (단일 조회)

    List<User> getAll(); // Read (전체 조회)

    void delete(Long userId); // Delete

}
