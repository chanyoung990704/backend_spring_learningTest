package org.example.restfulblogflatform.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.restfulblogflatform.entity.Post;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String username; // 작성자 이름
    private Long userId;
    private LocalDateTime createdAt;

    public static PostResponse of(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getUser().getId(),
                post.getCreatedDate()
        );
    }
}
