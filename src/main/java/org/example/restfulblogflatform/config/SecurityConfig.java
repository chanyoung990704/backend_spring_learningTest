package org.example.restfulblogflatform.config;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Spring Security 설정 클래스.
 * 이 클래스는 애플리케이션의 보안 정책을 정의하며, JWT 기반 인증 및 Stateless 세션 관리를 설정합니다.
 */
@Configuration // Spring에서 설정 클래스로 인식되도록 지정
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter; // JWT 요청 필터
    private final UserDetailsService userDetailsService; // 사용자 정보를 로드하는 서비스

    /**
     * SecurityFilterChain 빈 등록.
     * HTTP 요청에 대한 보안 정책을 정의합니다.
     *
     * @param http HttpSecurity 객체로, 보안 설정을 구성하는 데 사용됩니다.
     * @return SecurityFilterChain - HTTP 보안 필터 체인
     * @throws Exception 예외가 발생할 수 있음
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF 비활성화 (JWT 사용 시 필요 없음)
                .cors(withDefaults()) // CORS 활성화 (기본 설정 사용)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // 모든 요청을 인증 없이 허용
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless 세션 정책 (JWT 사용)
                );

        // UsernamePasswordAuthenticationFilter 앞에 JWT 요청 필터 추가
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // 보안 필터 체인 반환
    }

    /**
     * PasswordEncoder 빈 등록.
     * 비밀번호를 암호화하거나 검증할 때 사용됩니다.
     *
     * @return BCryptPasswordEncoder - BCrypt 알고리즘 기반의 비밀번호 인코더
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 객체 반환
    }

    /**
     * AuthenticationManager 빈 등록.
     *
     * @param authenticationConfiguration Spring Security가 제공하는 AuthenticationConfiguration 객체로,
     *                                     내부적으로 AuthenticationManager를 구성 및 관리합니다.
     * @return AuthenticationManager - 인증 요청을 처리하는 AuthenticationManager 객체
     * @throws Exception 예외가 발생할 수 있음
     *
     * @description
     * `AuthenticationManager`는 인증 요청(예: `UsernamePasswordAuthenticationToken`)을 처리하는 핵심 컴포넌트입니다.
     * 이 메서드는 AuthenticationConfiguration을 통해 자동으로 생성된 AuthenticationManager를 가져와 빈으로 등록합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Spring Security가 자동 생성한 AuthenticationManager 반환
    }

    /**
     * AuthenticationProvider 빈 등록.
     *
     * @param passwordEncoder 비밀번호 암호화를 위한 PasswordEncoder (ex: BCryptPasswordEncoder)
     * @return DaoAuthenticationProvider - 사용자 인증 처리를 위한 AuthenticationProvider 구현체 반환
     *
     * @description
     * `DaoAuthenticationProvider`는 UserDetailsService를 통해 사용자 정보를 로드하고,
     * PasswordEncoder를 사용하여 비밀번호 검증을 처리합니다. 이를 통해 사용자 인증 로직이 구성됩니다.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // 사용자 정보를 로드하는 UserDetailsService 설정
        authProvider.setUserDetailsService(userDetailsService);

        // 비밀번호 검증을 위한 PasswordEncoder 설정
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider; // DaoAuthenticationProvider 반환
    }
}
