package org.example.restfulblogflatform.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.entity.User;
import org.example.restfulblogflatform.service.post.PostService;
import org.example.restfulblogflatform.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PostService postService;
    private final UserService userService;
    private final CommentValidator commentValidator; // 댓글 검증 로직

    @Override
    @Transactional
    public Comment addComment(Long postId, Long userId, String content) {
        Post post = postService.getPost(postId);
        User user = userService.get(userId);

        // 댓글 생성
        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .build();

        // 양방향 연관 관계 설정
        post.addComment(comment);

        return comment;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        // 댓글 가져오기 및 검증
        Comment comment = commentValidator.getCommentOrThrow(commentId);

        // 양방향 관계 해제
        comment.getPost().removeComment(comment);
    }
}