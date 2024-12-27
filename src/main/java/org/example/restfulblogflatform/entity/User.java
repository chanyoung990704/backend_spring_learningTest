package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // 정적 팩토리 메서드
    public static User createUser(String username, String password, String email) {
        User user = new User();
        user.username = username;
        user.password = password;
        user.email = email;
        user.posts = new ArrayList<>();
        return user;
    }

    // 게시글 추가 메서드
    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    // 게시글 제거 메서드
    public void removePost(Post post) {
        posts.remove(post);
        post.setUser(null);
    }
}

