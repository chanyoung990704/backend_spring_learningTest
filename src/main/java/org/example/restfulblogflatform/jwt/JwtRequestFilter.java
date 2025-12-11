package org.example.restfulblogflatform.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 요청 필터 클래스.
 * 매 요청마다 실행되며, HTTP 요청의 Authorization 헤더에서 JWT를 추출하고 검증하여 사용자를 인증합니다.
 */
@Component // Spring의 Bean으로 등록
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // JWT 토큰 처리 유틸리티 클래스
    private final UserDetailsService userDetailsService; // 사용자 정보를 로드하는 서비스

    /**
     * 요청(Request)마다 실행되는 필터 메서드.
     * Authorization 헤더에서 JWT를 추출하고, 유효성을 검증한 후 SecurityContext에 인증 객체를 설정합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더에서 JWT 토큰 추출
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null; // JWT에서 추출한 사용자 이름
        String jwt = null; // 추출된 JWT 토큰

        // "Authorization" 헤더가 존재하고 "Bearer "로 시작하는 경우 JWT 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분만 추출
            try {
                username = jwtUtil.extractUsername(jwt); // JWT에서 사용자 이름(username) 추출
            } catch (Exception ignored) {
                username = null;
            }
        }

        // SecurityContext에 인증 객체가 없는 경우에만 JWT 인증 수행
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 사용자 정보를 로드 (UserDetailsService 사용)
            var userDetails = userDetailsService.loadUserByUsername(username);

            // JWT 토큰이 유효한 경우 SecurityContext에 인증 객체 설정
            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); // 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(authToken); // SecurityContext에 인증 객체 설정
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
