package org.example.restfulblogflatform.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * JWT 토큰 생성 및 검증을 처리하는 유틸리티 클래스.
 * JWT를 생성하고, 사용자 이름 추출, 유효성 검사, 만료 여부 확인 등의 기능을 제공합니다.
 */
@Component // Spring의 Bean으로 등록
public class JwtUtil {

    // 로그아웃된 토큰을 저장하는 Set (실제 운영환경에서는 Redis)
    private final Set<String> blacklistedTokens = new HashSet<>();

    // 비밀 키(SECRET_KEY)를 생성 (HS256 알고리즘 사용)
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param username 사용자 이름
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 토큰의 주체(subject)로 사용자 이름 설정
                .setIssuedAt(new Date()) // 토큰 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 만료 시간 설정 (10시간)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // 서명(Signature) 생성
                .compact(); // 최종적으로 JWT 문자열 반환
    }

    /**
     * JWT 토큰에서 사용자 이름(username)을 추출합니다.
     *
     * @param token JWT 토큰 문자열
     * @return 추출된 사용자 이름
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject(); // 클레임에서 subject(사용자 이름) 추출
    }

    /**
     * JWT 토큰에서 모든 클레임(Claims)을 추출합니다.
     *
     * @param token JWT 토큰 문자열
     * @return 추출된 클레임 객체
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // 서명 검증을 위한 비밀 키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱 및 서명 검증
                .getBody(); // 클레임 반환
    }


    /**
     * 토큰을 블랙리스트에 추가하여 무효화합니다.
     *
     * @param token 무효화할 JWT 토큰
     */
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }


    /**
     * JWT 토큰의 유효성을 검사합니다.
     *
     * @param token JWT 토큰 문자열
     * @param username 비교할 사용자 이름
     * @return 토큰이 유효하면 true, 그렇지 않으면 false 반환
     */
    public boolean isTokenValid(String token, String username) {
        // 블랙리스트 체크
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        final String extractedUsername = extractUsername(token); // 토큰에서 사용자 이름 추출
        return (extractedUsername.equals(username) && !isTokenExpired(token)); // 사용자 이름 일치 및 만료 여부 확인
    }

    /**
     * JWT 토큰의 만료 여부를 확인합니다.
     *
     * @param token JWT 토큰 문자열
     * @return 만료되었으면 true, 그렇지 않으면 false 반환
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date()); // 현재 시간과 만료 시간 비교
    }
}
