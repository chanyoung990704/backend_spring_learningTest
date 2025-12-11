package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean revoked;

    public static RefreshToken create(Long userId, String token, LocalDateTime expiryDate) {
        return RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiryDate(expiryDate)
                .revoked(false)
                .build();
    }

    public void revoke() {
        this.revoked = true;
    }
}
