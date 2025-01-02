package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 댓글(Comment) 엔티티 클래스.
 * 게시글(Post)과 사용자(User)에 연관된 댓글 데이터를 관리합니다.
 */
@Entity // JPA 엔티티로 지정
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Lombok 어노테이션: 기본 생성자를 생성하되, 접근 수준을 PROTECTED로 제한
@AllArgsConstructor // Lombok 어노테이션: 모든 필드를 포함하는 생성자를 자동 생성
@Table(name = "comments") // 데이터베이스 테이블 이름을 "comments"로 지정
public class Comment extends BaseEntity { // BaseEntity를 상속받아 생성/수정 시간 관리

    @Id // 기본 키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 증가(AUTO_INCREMENT) 방식으로 설정
    private Long id; // 댓글 ID

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정 (댓글 -> 게시글)
    @JoinColumn(name = "post_id", nullable = false) // 외래 키(foreign key)를 "post_id"로 지정
    private Post post; // 댓글이 속한 게시글

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정 (댓글 -> 사용자)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키(foreign key)를 "user_id"로 지정
    private User user; // 댓글 작성자

    @Column(nullable = false, columnDefinition = "TEXT") // 내용은 null 불가, TEXT 타입으로 저장
    private String content; // 댓글 내용

    /**
     * 댓글(Comment) 객체를 생성하는 정적 팩토리 메서드.
     * 양방향 연관관계를 설정하고, 댓글 객체를 생성합니다.
     *
     * @param user 댓글 작성자(User)
     * @param post 댓글이 속한 게시글(Post)
     * @param content 댓글 내용
     * @return 생성된 Comment 객체
     */
    public static Comment createComment(User user, Post post, String content) {
        Comment comment = new Comment(); // 새로운 Comment 객체 생성
        comment.user = user; // 작성자 설정
        comment.post = post; // 게시글 설정
        comment.content = content; // 내용 설정

        // 양방향 연관관계 설정 (Post 엔티티에 댓글 추가)
        post.addComment(comment);
        return comment; // 생성된 Comment 객체 반환
    }

    /**
     * 댓글이 속한 게시글(Post)을 설정하는 메서드.
     * 양방향 연관관계를 유지하기 위해 사용됩니다.
     *
     * @param post 댓글이 속할 게시글(Post)
     */
    public void setPost(Post post) {
        this.post = post;
    }


    /**
     * 댓글 내용을 업데이트하는 메서드.
     *
     * @param content 새로운 댓글 내용
     * @implNote 댓글 내용의 유효성 검사는 DTO에서 @Validation으로 처리됩니다.
     */
    public void updateContent(String content) {
        this.content = content;
    }

}