package org.example.restfulblogflatform.security;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security에서 사용자 인증을 처리하기 위한 커스텀 UserDetailsService 구현체.
 * 이메일을 기반으로 사용자 정보를 로드하고, 인증에 필요한 UserDetails 객체를 반환합니다.
 */
@Service // Spring의 Service 계층으로 등록
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 사용자 데이터를 처리하는 JPA Repository

    /**
     * 이메일을 기반으로 사용자 정보를 로드합니다.
     *
     * @param email 사용자 이메일 (Spring Security에서 username으로 사용)
     * @return UserDetails 객체 (Spring Security에서 인증에 사용)
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일로 사용자 조회. 없으면 UsernameNotFoundException 예외 발생
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // 조회된 사용자 정보를 기반으로 CustomUserDetails 객체 반환
        return new CustomUserDetails(user);
    }
}
