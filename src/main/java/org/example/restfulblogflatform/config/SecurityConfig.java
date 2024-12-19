package org.example.restfulblogflatform.config;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/login", "api/signup").permitAll() // 로그인 엔드포인트 인증 불필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 사용 시 Stateless 설정
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * AuthenticationManager 빈 등록
     *
     * @param authenticationConfiguration Spring Security가 제공하는 AuthenticationConfiguration
     *                                     객체로, 내부적으로 AuthenticationManager를 구성 및 관리함.
     * @return AuthenticationManager를 반환
     * @throws Exception 예외가 발생할 수 있음
     *
     * @description
     * `AuthenticationManager`는 Spring Security에서 인증 요청(예: `UsernamePasswordAuthenticationToken`)을 처리하는
     * 핵심 컴포넌트입니다. 이 메서드는 AuthenticationConfiguration을 통해 자동으로 생성된
     * AuthenticationManager를 가져와 사용할 수 있도록 빈을 등록합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Spring Security가 자동 생성
    }

    /**
     * AuthenticationProvider 빈 등록
     *
     * @param passwordEncoder 비밀번호 암호화를 위한 PasswordEncoder (ex: BCryptPasswordEncoder)
     * @return DaoAuthenticationProvider를 반환
     *
     * @description
     * AuthenticationProvider 인터페이스의 구현체인 `DaoAuthenticationProvider`를 구성 및 등록합니다.
     * DaoAuthenticationProvider를 사용하면 UserDetailsService를 통해 사용자 정보를 로드하고,
     * 비밀번호 검증은 PasswordEncoder를 사용하여 처리합니다.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // 사용자 정보를 로드하는 UserDetailsService 설정
        authProvider.setUserDetailsService(userDetailsService);

        // 비밀번호 검증을 위한 PasswordEncoder 설정
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider; // AuthenticationProvider를 반환
    }
}
