package org.example.restfulblogflatform.service.post;

import org.example.restfulblogflatform.entity.Post;

import java.util.List;

public interface PostService {
    Post createPost(String title, String content, Long userId);
    Post getPost(Long postId);
    List<Post> getAllPosts();
    Post updatePost(Long postId, String title, String content);
    void deletePost(Long postId);
}
