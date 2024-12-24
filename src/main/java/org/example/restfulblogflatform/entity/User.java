package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    // 업데이트용 메서드
//    public User update(UpdateUserDto dto) {
//        if(dto.getUsername() != null) this.username = dto.getUsername();
//        if(dto.getPassword() != null) this.password = dto.getPassword();
//        if(dto.getEmail() != null) this.email = dto.getEmail();
//        return this;
//    }
}

