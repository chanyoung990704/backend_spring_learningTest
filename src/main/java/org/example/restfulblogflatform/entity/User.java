package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자(User) 엔티티 클래스.
 * 게시글(Post)과 연관된 사용자 데이터를 관리합니다.
 */
@Entity // JPA 엔티티로 지정
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Lombok 어노테이션: 기본 생성자를 생성하되, 접근 수준을 PROTECTED로 제한
@Table(name = "users") // 데이터베이스 테이블 이름을 "users"로 지정
public class User extends BaseEntity { // BaseEntity를 상속받아 생성/수정 시간 관리

    @Id // 기본 키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 증가(AUTO_INCREMENT) 방식으로 설정
    private Long id; // 사용자 ID

    @Column(nullable = false) // null 불가
    private String username; // 사용자 이름

    @Column(nullable = false) // null 불가
    private String password; // 사용자 비밀번호

    @Column(unique = true, nullable = false) // 이메일은 고유(unique)하면서 null 불가
    private String email; // 사용자 이메일

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // 일대다 관계 설정 (사용자 -> 게시글)
    // CascadeType.ALL: 사용자가 삭제되면 관련 게시글도 함께 삭제
    // orphanRemoval: 고아 객체(연관관계가 끊어진 게시글)를 자동으로 제거
    private List<Post> posts; // 사용자가 작성한 게시글 목록

    /**
     * 사용자(User) 객체를 생성하는 정적 팩토리 메서드.
     *
     * @param username 사용자 이름
     * @param password 사용자 비밀번호
     * @param email 사용자 이메일
     * @return 생성된 User 객체
     */
    public static User createUser(String username, String password, String email) {
        User user = new User(); // 새로운 User 객체 생성
        user.username = username; // 사용자 이름 설정
        user.password = password; // 비밀번호 설정
        user.email = email; // 이메일 설정
        user.posts = new ArrayList<>(); // 빈 게시글 리스트 초기화
        return user; // 생성된 User 객체 반환
    }

    /**
     * 게시글(Post)을 추가하는 메서드.
     * 양방향 연관관계를 유지하기 위해 사용됩니다.
     *
     * @param post 추가할 게시글(Post)
     */
    public void addPost(Post post) {
        posts.add(post); // 게시글 리스트에 추가
        post.setUser(this); // 게시글의 작성자(User)를 현재 객체로 설정
    }

    /**
     * 게시글(Post)을 제거하는 메서드.
     * 양방향 연관관계를 유지하기 위해 사용됩니다.
     *
     * @param post 제거할 게시글(Post)
     */
    public void removePost(Post post) {
        posts.remove(post); // 게시글 리스트에서 제거
        post.setUser(null); // 게시글의 작성자(User)를 null로 설정
    }
}

