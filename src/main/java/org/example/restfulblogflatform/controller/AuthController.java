package org.example.restfulblogflatform.controller;

import lombok.*;
import org.example.restfulblogflatform.exception.JwtErrorCode;
import org.example.restfulblogflatform.exception.jwt.JwtException;
import org.example.restfulblogflatform.exception.response.ErrorResponse;
import org.example.restfulblogflatform.jwt.JwtUtil;
import org.example.restfulblogflatform.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
     * 사용자 로그아웃을 처리하는 컨트롤러 메서드.
     * JWT 토큰을 무효화하고 로그아웃 처리를 수행합니다.
     *
     * @param token Authorization 헤더에서 전달받은 JWT 토큰 (Bearer 토큰)
     * @return ResponseEntity<ErrorResponse<String>> 로그아웃 처리 결과를 포함한 응답
     * @throws JwtException 토큰 처리 중 발생하는 예외:
     *         - TOKEN_MISSING: 토큰이 제공되지 않은 경우
     *         - INVALID_TOKEN_FORMAT: 잘못된 토큰 형식
     *         - TOKEN_INVALIDATION_ERROR: 토큰 무효화 처리 실패
     */
    @PostMapping("/logout")
    public ResponseEntity<ErrorResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String token) {

        // 1. 토큰 존재 여부 검증
        if (token == null) {
            // 토큰이 제공되지 않은 경우 예외 발생
            throw new JwtException(JwtErrorCode.TOKEN_MISSING);
        }

        // 2. Bearer 토큰 형식 검증
        if (!token.startsWith("Bearer ")) {
            // Bearer 형식이 아닌 경우 예외 발생
            throw new JwtException(JwtErrorCode.INVALID_TOKEN_FORMAT);
        }

        try {
            // 3. Bearer 접두사 제거하여 실제 JWT 토큰 추출
            String jwt = token.substring(7);

            // 4. JWT 토큰 무효화 처리
            jwtUtil.invalidateToken(jwt);

            // 5. 로그아웃 성공 응답 생성
            ErrorResponse<String> response = ErrorResponse.<String>builder()
                    .status(HttpStatus.OK)                // HTTP 상태 코드 설정
                    .message("로그아웃 성공")              // 성공 메시지
                    .data(null)                          // 추가 데이터 없음
                    .timestamp(LocalDateTime.now())      // 현재 시간
                    .build();

            // 6. 성공 응답 반환
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 7. 토큰 무효화 처리 중 발생한 예외 처리
            // 예외 발생 시 TOKEN_INVALIDATION_ERROR로 래핑하여 throw
            throw new JwtException(JwtErrorCode.TOKEN_INVALIDATION_ERROR);
        }
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

    /**
     * API 응답에 사용되는 메시지 래퍼 클래스
     * 클라이언트에게 전달할 메시지를 포함합니다.
     */
    @Getter
    @AllArgsConstructor
    static class MessageResponse {
        private String message; // 응답 메시지 내용
    }
}
