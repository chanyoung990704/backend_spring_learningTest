package org.example.restfulblogflatform.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.restfulblogflatform.entity.Post;

import java.time.LocalDateTime;

/**
 * 게시글 응답 데이터를 담는 DTO(Data Transfer Object).
 * 클라이언트에게 게시글 정보를 반환할 때 사용됩니다.
 */
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
@AllArgsConstructor(staticName = "of") // Lombok 어노테이션: 모든 필드를 포함하는 생성자를 생성하며, static 팩토리 메서드("of")도 제공
public class PostResponse {

    private Long id; // 게시글 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String username; // 작성자 이름
    private Long userId; // 작성자 ID
    private LocalDateTime createdAt; // 게시글 생성 시간

    /**
     * Post 엔티티를 PostResponse DTO로 변환하는 정적 팩토리 메서드.
     *
     * @param post Post 엔티티 객체
     * @return PostResponse - 변환된 응답 객체
     */
    public static PostResponse of(Post post) {
        return new PostResponse(
                post.getId(), // 게시글 ID
                post.getTitle(), // 게시글 제목
                post.getContent(), // 게시글 내용
                post.getUser().getUsername(), // 작성자 이름 (User 엔티티에서 가져옴)
                post.getUser().getId(), // 작성자 ID (User 엔티티에서 가져옴)
                post.getCreatedDate() // 생성 시간
        );
    }
}

