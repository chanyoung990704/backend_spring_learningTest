package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(nullable = false)
    private int viewCount = 0;

    // 정적 팩토리 메서드
    public static Post createPost(User user, String title, String content) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        post.viewCount = 0; // 초기 조회수 설정
        post.comments = new ArrayList<>();
        post.setUser(user);
        user.addPost(post); // 양방향 연관관계 설정
        return post;
    }

    // 연관관계를 위한 UserSetter
    public void setUser(User user) {
        this.user = user;
    }

    // 댓글 추가 메서드 (양방향 연관관계 편의 메서드)
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    // 댓글 제거 메서드 (양방향 연관관계 편의 메서드)
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    // 게시글 업데이트 메서드
    public Post update(String title, String content) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
        if (content != null && !content.isEmpty()) {
            this.content = content;
        }
        return this;
    }
}