package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.repository.PostRepository;
import org.example.restfulblogflatform.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostValidator postValidator; // Post 관련 검증 로직

    @Override
    @Transactional
    public Post createPost(String title, String content, Long userId) {
        // 사용자 존재 여부 확인 (UserValidator를 통해 처리 가능)
        User user = userService.get(userId);

        // 게시글 생성
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();

        // 등록
        user.addPost(post);

        return post;
    }

    @Override
    public Post getPost(Long postId) {
        // 게시글 가져오기 (검증 포함)
        return postValidator.getPostOrThrow(postId);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, String title, String content) {
        // 게시글 가져오기 및 검증
        Post post = postValidator.getPostOrThrow(postId);

        // 업데이트 수행
        post.update(title, content);
        return post;
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Post post = postValidator.getPostOrThrow(postId);
        post.getUser().removePost(post);
    }
}