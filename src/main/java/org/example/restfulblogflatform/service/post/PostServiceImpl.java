package org.example.restfulblogflatform.service.post;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
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
    private final PostValidator postValidator;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest, Long userId) {


        User user = userService.get(userId);

        // 게시글 생성 & 연관관계 등록
        Post post = Post.createPost(user, postRequest.getTitle(), postRequest.getContent());

        // 저장 후 DTO 변환하여 반환
        return PostResponse.of(postRepository.save(post));
    }

    @Override
    public Post getPost(Long postId) {
        // 게시글 가져오기 (검증 포함)
        Post post = postValidator.getPostOrThrow(postId);
        return post;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long postId, String title, String content) {
        // 게시글 가져오기 및 검증
        Post post = postValidator.getPostOrThrow(postId);

        // 업데이트 수행
        post.update(title, content);
        return PostResponse.of(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        // 게시글 가져오기 및 검증
        Post post = postValidator.getPostOrThrow(postId);

        // 연관관계 제거 및 삭제 (CascadeType.REMOVE를 사용하는 경우 생략 가능)
        post.getUser().removePost(post);
        postRepository.delete(post);
    }
}
