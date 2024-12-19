package org.example.restfulblogflatform.security;

import org.example.restfulblogflatform.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 없음
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired(); // 외부 로직으로 확인
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked(); // 외부 로직으로 확인
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired(); // 외부 로직으로 확인
    }

    @Override
    public boolean isEnabled() {
        return isActiveAccount(); // 외부 로직으로 확인
    }

    // 외부 서비스로부터 상태 확인
    private boolean isAccountExpired() {
        // 비즈니스 로직으로 계정 만료 여부 확인
        return false; // 예: 항상 만료되지 않았다고 간주
    }

    private boolean isAccountLocked() {
        // 비즈니스 로직으로 계정 잠금 여부 확인
        return false; // 예: 항상 잠겨 있지 않다고 간주
    }

    private boolean isCredentialsExpired() {
        // 비즈니스 로직으로 자격 증명 만료 여부 확인
        return false; // 예: 항상 만료되지 않았다고 간주
    }

    private boolean isActiveAccount() {
        // 비즈니스 로직으로 활성화 여부 확인
        return true; // 예: 항상 활성 상태로 간주
    }
}
