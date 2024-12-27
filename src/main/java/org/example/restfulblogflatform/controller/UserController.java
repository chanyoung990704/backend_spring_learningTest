package org.example.restfulblogflatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.user.request.UserSignUpRequestDto;
import org.example.restfulblogflatform.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자(User) 관련 요청을 처리하는 컨트롤러.
 * 이 컨트롤러는 회원가입과 같은 사용자 관련 기능을 제공합니다.
 */
@RestController // RESTful 웹 서비스를 위한 컨트롤러로 지정
@RequestMapping("/api/users") // "/api/users" 경로로 들어오는 요청을 처리
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class UserController {

    private final UserService userService; // 사용자 관련 비즈니스 로직을 처리하는 서비스

    /**
     * 회원가입 요청 처리.
     *
     * @param request 회원가입 요청 데이터 (사용자 이름, 이메일, 비밀번호 등)
     * @return ResponseEntity<Long> - 생성된 사용자의 ID를 포함한 응답 (HTTP 200 OK)
     */
    @PostMapping("/signup") // POST 요청으로 "/api/users/signup" 경로 처리
    public ResponseEntity<Long> signUp(@RequestBody @Valid UserSignUpRequestDto request) {
        // 회원가입 요청 데이터를 서비스로 전달하여 사용자 생성
        Long id = userService.add(request);
        return ResponseEntity.ok(id); // 생성된 사용자 ID를 HTTP 200 응답으로 반환
    }
}
