package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    // 게시글 추가 메서드 (양방향 연관관계 편의 메서드)
    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    // 게시글 제거 메서드 (양방향 연관관계 편의 메서드)
    public void removePost(Post post) {
        posts.remove(post);
        post.setUser(null);
    }

    // 업데이트용 메서드
//    public User update(UpdateUserDto dto) {
//        if(dto.getUsername() != null) this.username = dto.getUsername();
//        if(dto.getPassword() != null) this.password = dto.getPassword();
//        if(dto.getEmail() != null) this.email = dto.getEmail();
//        return this;
//    }
}

