package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.repository.PostRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostValidator {

    private final PostRepository postRepository;

    // 게시글 존재 여부 검증
    public void validatePostExists(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found with id: " + postId);
        }
    }

    // 게시글 가져오거나 예외 발생
    public Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));
    }
}
