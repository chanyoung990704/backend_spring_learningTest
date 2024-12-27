package org.example.restfulblogflatform.service.post;

import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
import org.example.restfulblogflatform.entity.Post;

import java.util.List;

public interface PostService {

    /**
     * 게시글 생성
     * @param postRequest 게시글 요청 DTO
     * @return 생성된 게시글의 응답 DTO
     */
    PostResponse createPost(PostRequest postRequest, Long userId);

    /**
     * 게시글 단일 조회
     * @param postId 조회할 게시글 ID
     * @return 조회된 게시글의 응답 DTO
     */
    Post getPost(Long postId);

    /**
     * 모든 게시글 조회
     * @return 모든 게시글의 응답 DTO 리스트
     */
    List<Post> getAllPosts();

    /**
     * 게시글 업데이트
     * @param postId 업데이트할 게시글 ID
     * @param title 새로운 제목
     * @param content 새로운 내용
     * @return 업데이트된 게시글의 응답 DTO
     */
    PostResponse updatePost(Long postId, String title, String content);

    /**
     * 게시글 삭제
     * @param postId 삭제할 게시글 ID
     */
    void deletePost(Long postId);
}
