package org.example.restfulblogflatform.security;

import org.example.restfulblogflatform.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security에서 사용자 인증 정보를 관리하기 위한 커스텀 UserDetails 구현체.
 * 애플리케이션의 사용자 엔티티(User)를 기반으로 인증 및 계정 상태를 처리합니다.
 */
public class CustomUserDetails implements UserDetails {

    private final User user; // 애플리케이션의 사용자 엔티티

    /**
     * CustomUserDetails 생성자.
     *
     * @param user 애플리케이션의 사용자 엔티티
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * 사용자 이름(Username)을 반환합니다.
     *
     * @return 사용자 이름 (User 엔티티의 username 필드)
     */
    public String getName() {
        return user.getUsername();
    }

    /**
     * 사용자 ID를 반환합니다.
     *
     * @return 사용자 ID (User 엔티티의 id 필드)
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * 사용자의 권한 목록을 반환합니다.
     *
     * @return 빈 권한 목록 (현재 권한 없음)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 없음
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     *
     * @return 사용자 비밀번호 (User 엔티티의 password 필드)
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자의 이메일(Username)을 반환합니다.
     *
     * @return 사용자 이메일 (User 엔티티의 email 필드)
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 계정이 만료되지 않았는지 여부를 반환합니다.
     *
     * @return true(만료되지 않음), false(만료됨)
     */
    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired(); // 외부 로직으로 확인
    }

    /**
     * 계정이 잠겨 있지 않은지 여부를 반환합니다.
     *
     * @return true(잠겨 있지 않음), false(잠김)
     */
    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked(); // 외부 로직으로 확인
    }

    /**
     * 자격 증명이 만료되지 않았는지 여부를 반환합니다.
     *
     * @return true(만료되지 않음), false(만료됨)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired(); // 외부 로직으로 확인
    }

    /**
     * 계정이 활성화되었는지 여부를 반환합니다.
     *
     * @return true(활성화됨), false(비활성화됨)
     */
    @Override
    public boolean isEnabled() {
        return isActiveAccount(); // 외부 로직으로 확인
    }

    /**
     * 계정 만료 여부를 확인하는 비즈니스 로직.
     *
     * @return true(만료됨), false(만료되지 않음)
     */
    private boolean isAccountExpired() {
        // 비즈니스 로직으로 계정 만료 여부 확인
        return false; // 예: 항상 만료되지 않았다고 간주
    }

    /**
     * 계정 잠금 여부를 확인하는 비즈니스 로직.
     *
     * @return true(잠김), false(잠겨 있지 않음)
     */
    private boolean isAccountLocked() {
        // 비즈니스 로직으로 계정 잠금 여부 확인
        return false; // 예: 항상 잠겨 있지 않다고 간주
    }

    /**
     * 자격 증명 만료 여부를 확인하는 비즈니스 로직.
     *
     * @return true(만료됨), false(만료되지 않음)
     */
    private boolean isCredentialsExpired() {
        // 비즈니스 로직으로 자격 증명 만료 여부 확인
        return false; // 예: 항상 만료되지 않았다고 간주
    }

    /**
     * 계정 활성화 여부를 확인하는 비즈니스 로직.
     *
     * @return true(활성화됨), false(비활성화됨)
     */
    private boolean isActiveAccount() {
        // 비즈니스 로직으로 활성화 여부 확인
        return true; // 예: 항상 활성 상태로 간주
    }
}
