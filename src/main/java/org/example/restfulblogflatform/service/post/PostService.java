package org.example.restfulblogflatform.service.post;

import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
import org.example.restfulblogflatform.entity.Post;

import java.util.List;

/**
 * 게시글(Post) 관련 비즈니스 로직을 처리하기 위한 서비스 인터페이스.
 * 게시글 생성, 조회, 수정, 삭제와 같은 주요 기능을 정의합니다.
 */
public interface PostService {

    /**
     * 게시글을 생성합니다.
     *
     * @param postRequest 게시글 생성 요청 DTO (제목, 내용 등 포함)
     * @param userId 게시글 작성자의 사용자 ID
     * @return 생성된 게시글의 응답 DTO
     */
    PostResponse add(PostRequest postRequest, Long userId);

    /**
     * 특정 게시글을 조회합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 엔티티
     */
    Post get(Long postId);

    /**
     * 모든 게시글을 조회합니다.
     *
     * @return 모든 게시글 엔티티 리스트
     */
    List<Post> getAll();

    /**
     * 특정 게시글을 업데이트합니다.
     *
     * @param postId 업데이트할 게시글의 ID
     * @param title 새로운 제목
     * @param content 새로운 내용
     * @return 업데이트된 게시글의 응답 DTO
     */
    PostResponse update(Long postId, String title, String content);

    /**
     * 특정 게시글을 삭제합니다.
     *
     * @param postId 삭제할 게시글의 ID
     */
    void delete(Long postId);
}
