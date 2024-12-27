package org.example.restfulblogflatform.controller;

import lombok.*;
import org.example.restfulblogflatform.jwt.JwtUtil;
import org.example.restfulblogflatform.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager; // 인증 처리를 위한 AuthenticationManager
    private final JwtUtil jwtUtil; // JWT 토큰 생성 및 검증 유틸리티 클래스

    /**
     * 로그인 요청 처리 메서드.
     * 클라이언트가 전달한 사용자명과 비밀번호를 인증하고, 성공 시 JWT 토큰을 생성하여 반환합니다.
     *
     * @param authRequest 클라이언트가 전달한 사용자명과 비밀번호를 포함하는 요청 객체
     * @return ResponseEntity<LoginResponse> - JWT 토큰과 사용자 정보를 포함한 응답 객체
     */
    @PostMapping("/login") // "/auth/login" 경로로 POST 요청 처리
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        // 사용자명과 비밀번호를 기반으로 인증 처리
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        // 인증 정보를 SecurityContext에 저장 (Spring Security에서 관리)
        SecurityContextHolder.getContext().setAuthentication(auth);

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(authRequest.getUsername());

        // 인증된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        // 로그인 응답 객체 생성 (JWT 토큰 및 사용자 정보 포함)
        LoginResponse response = new LoginResponse(token, userDetails.getName(), userDetails.getId());

        // HTTP 200 OK 응답 반환
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 요청 데이터를 담는 내부 클래스.
     * 클라이언트에서 전달받은 사용자명(username)과 비밀번호(password)를 포함합니다.
     */
    @Getter // Lombok 어노테이션: Getter 메서드 자동 생성
    @Setter // Lombok 어노테이션: Setter 메서드 자동 생성
    @NoArgsConstructor // Lombok 어노테이션: 기본 생성자 자동 생성
    static class AuthRequest {
        private String username; // 사용자명
        private String password; // 비밀번호
    }

    /**
     * 로그인 응답 데이터를 담는 내부 클래스.
     * 클라이언트에게 반환할 JWT 토큰과 사용자 정보를 포함합니다.
     */
    @Getter // Lombok 어노테이션: Getter 메서드 자동 생성
    @Setter // Lombok 어노테이션: Setter 메서드 자동 생성
    class LoginResponse {
        private String token; // JWT 토큰
        private String username; // 사용자 이름
        private Long userId; // 사용자 ID

        /**
         * LoginResponse 생성자.
         *
         * @param token JWT 토큰
         * @param username 사용자 이름
         * @param userId 사용자 ID
         */
        public LoginResponse(String token, String username, Long userId) {
            this.token = token;
            this.username = username;
            this.userId = userId;
        }
    }
}
