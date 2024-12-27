package org.example.restfulblogflatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.post.request.PostRequest;
import org.example.restfulblogflatform.dto.post.response.PostResponse;
import org.example.restfulblogflatform.entity.Post;
import org.example.restfulblogflatform.security.CustomUserDetails;
import org.example.restfulblogflatform.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글(Post) 관련 요청을 처리하는 컨트롤러.
 * 이 컨트롤러는 게시글 생성, 조회, 수정, 삭제와 같은 CRUD 기능을 제공합니다.
 */
@RestController // RESTful 웹 서비스를 위한 컨트롤러로 지정
@RequestMapping("/api/posts") // "/api/posts" 경로로 들어오는 요청을 처리
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class PostController {

    private final PostService postService; // 게시글 관련 비즈니스 로직을 처리하는 서비스

    /**
     * 게시글 생성 요청 처리.
     *
     * @param request 게시글 생성 요청 데이터 (제목, 내용 등)
     * @param userDetails 인증된 사용자 정보
     * @return ResponseEntity<PostResponse> - 생성된 게시글 정보를 포함한 응답 (HTTP 201 Created)
     */
    @PostMapping // POST 요청 처리
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 인증된 사용자의 ID를 사용하여 게시글 생성
        PostResponse response = postService.add(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201 응답 반환
    }

    /**
     * 특정 게시글 단일 조회 요청 처리.
     *
     * @param postId 조회할 게시글의 ID
     * @return ResponseEntity<PostResponse> - 조회된 게시글 정보를 포함한 응답 (HTTP 200 OK)
     */
    @GetMapping("/{postId}") // GET 요청으로 특정 게시글 조회
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        // 서비스에서 게시글 조회
        Post post = postService.get(postId);
        // 조회된 게시글 정보를 응답 객체로 변환
        PostResponse response = PostResponse.of(post);
        return ResponseEntity.ok(response); // HTTP 200 응답 반환
    }

    /**
     * 모든 게시글 조회 요청 처리.
     *
     * @return ResponseEntity<List<PostResponse>> - 모든 게시글 정보를 포함한 응답 (HTTP 200 OK)
     */
    @GetMapping // GET 요청으로 모든 게시글 조회
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        // 모든 게시글을 조회하고 응답 객체로 변환
        List<PostResponse> responses = postService.getAll()
                .stream()
                .map(PostResponse::of) // Post를 PostResponse로 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses); // HTTP 200 응답 반환
    }

    /**
     * 특정 게시글 업데이트 요청 처리.
     *
     * @param postId 업데이트할 게시글의 ID
     * @param request 업데이트할 제목과 내용을 포함한 요청 데이터
     * @return ResponseEntity<PostResponse> - 업데이트된 게시글 정보를 포함한 응답 (HTTP 200 OK)
     */
    @PutMapping("/{postId}") // PUT 요청으로 특정 게시글 업데이트
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                                   @RequestBody @Valid PostRequest request) {
        // 서비스에서 제목과 내용을 업데이트
        PostResponse updatedResponse = postService.update(postId, request.getTitle(), request.getContent());
        return ResponseEntity.ok(updatedResponse); // HTTP 200 응답 반환
    }

    /**
     * 특정 게시글 삭제 요청 처리.
     *
     * @param postId 삭제할 게시글의 ID
     * @return ResponseEntity<Void> - 삭제 성공 시 HTTP 204 No Content 응답 반환
     */
    @DeleteMapping("/{postId}") // DELETE 요청으로 특정 게시글 삭제
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.delete(postId); // 서비스에서 게시글 삭제 처리
        return ResponseEntity.noContent().build(); // HTTP 204 응답 반환 (내용 없음)
    }
}


