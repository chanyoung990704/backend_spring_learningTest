package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글(Post) 엔티티 클래스.
 * 사용자(User)와 댓글(Comment)에 연관된 게시글 데이터를 관리합니다.
 */
@Entity // JPA 엔티티로 지정
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Lombok 어노테이션: 기본 생성자를 생성하되, 접근 수준을 PROTECTED로 제한
@Table(name = "posts") // 데이터베이스 테이블 이름을 "posts"로 지정
public class Post extends BaseEntity { // BaseEntity를 상속받아 생성/수정 시간 관리

    @Id // 기본 키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 증가(AUTO_INCREMENT) 방식으로 설정
    private Long id; // 게시글 ID

    @Column(nullable = false) // 제목은 null 불가
    private String title; // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT") // 내용은 null 불가, TEXT 타입으로 저장
    private String content; // 게시글 내용

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정 (게시글 -> 사용자)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키(foreign key)를 "user_id"로 지정
    private User user; // 게시글 작성자

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    // 일대다 관계 설정 (게시글 -> 댓글)
    // CascadeType.ALL: 게시글 삭제 시 관련 댓글도 함께 삭제
    // orphanRemoval: 고아 객체(연관관계가 끊어진 댓글)를 자동으로 제거
    private List<Comment> comments; // 게시글에 달린 댓글 목록

    @Column(nullable = false) // 조회수는 null 불가
    @Version // 낙관적 락 버전 관리 필드
    private int viewCount = 0; // 게시글 조회수 (기본값 0)

    /**
     * 게시글(Post) 객체를 생성하는 정적 팩토리 메서드.
     * 양방향 연관관계를 설정하고, 게시글 객체를 생성합니다.
     *
     * @param user 게시글 작성자(User)
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @return 생성된 Post 객체
     */
    public static Post createPost(User user, String title, String content) {
        Post post = new Post(); // 새로운 Post 객체 생성
        post.title = title; // 제목 설정
        post.content = content; // 내용 설정
        post.viewCount = 0; // 초기 조회수 설정
        post.comments = new ArrayList<>(); // 빈 댓글 리스트 초기화
        post.setUser(user); // 작성자 설정 및 연관관계 설정
        user.addPost(post); // 작성자(User)에 게시글 추가 (양방향 연관관계 설정)
        return post; // 생성된 Post 객체 반환
    }

    /**
     * 작성자(User)를 설정하는 메서드.
     * 양방향 연관관계를 유지하기 위해 사용됩니다.
     *
     * @param user 게시글 작성자(User)
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 댓글을 추가하는 메서드.
     * 양방향 연관관계를 유지하기 위해 사용됩니다.
     *
     * @param comment 추가할 댓글(Comment)
     */
    public void addComment(Comment comment) {
        comments.add(comment); // 댓글 리스트에 추가
        comment.setPost(this); // 댓글의 연관된 게시글(Post)을 설정
    }

    /**
     * 댓글을 제거하는 메서드.
     * 양방향 연관관계를 유지하기 위해 사용됩니다.
     *
     * @param comment 제거할 댓글(Comment)
     */
    public void removeComment(Comment comment) {
        comments.remove(comment); // 댓글 리스트에서 제거
        comment.setPost(null); // 댓글의 연관된 게시글(Post)을 null로 설정
    }

    /**
     * 게시글 정보를 업데이트하는 메서드.
     *
     * @param title 새로운 제목 (null 또는 빈 값이면 변경하지 않음)
     * @param content 새로운 내용 (null 또는 빈 값이면 변경하지 않음)
     * @return 업데이트된 Post 객체 자신을 반환 (메서드 체이닝 가능)
     */
    public Post update(String title, String content) {
        if (title != null && !title.isEmpty()) {
            this.title = title; // 제목 업데이트
        }
        if (content != null && !content.isEmpty()) {
            this.content = content; // 내용 업데이트
        }
        return this; // 업데이트된 Post 객체 반환
    }

    /**
     * 게시글 조회수를 1 증가시키는 메서드.
     */
    public void incrementViewCount() {
        this.viewCount++;
    }

}