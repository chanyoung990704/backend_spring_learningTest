package org.example.restfulblogflatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.post.request.PostRequestDto;
import org.example.restfulblogflatform.dto.post.response.PostResponseDto;
import org.example.restfulblogflatform.security.CustomUserDetails;
import org.example.restfulblogflatform.service.post.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 게시글(Post) 관련 요청을 처리하는 REST 컨트롤러.
 *
 * 이 컨트롤러는 게시글 생성, 조회, 수정, 삭제와 같은 CRUD(생성, 읽기, 갱신, 삭제) 기능을 제공합니다.
 * 클라이언트 요청을 처리하고, 적절한 응답을 반환합니다.
 */
@RestController // RESTful 웹 서비스를 위한 컨트롤러로 지정
@RequestMapping("/api/posts") // "/api/posts" 경로로 들어오는 요청을 처리
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성 (DI를 위한 Lombok 어노테이션)
public class PostController {

    private final PostService postService; // 게시글 관련 비즈니스 로직을 처리하는 서비스

    /**
     * 게시글 생성 API
     *
     * 인증된 사용자가 새로운 게시글을 생성합니다. 제목과 내용을 요청 데이터로 전달받아
     * 게시글을 생성하고, 생성된 게시글 정보를 응답합니다.
     *
     * @param request      게시글 생성 요청 데이터 (제목, 내용 포함)
     * @param userDetails  인증된 사용자 정보 (Spring Security에서 제공)
     * @return ResponseEntity<PostResponseDto> - 생성된 게시글 정보를 포함한 응답 (HTTP 201 Created)
     */
    @PostMapping // POST 요청 처리
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto request,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 인증된 사용자의 ID를 사용하여 게시글 생성
        PostResponseDto response = postService.add(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201 응답 반환
    }

    /**
     * 게시글 단일 조회 API
     *
     * 특정 게시글의 ID를 기반으로 게시글을 조회합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return ResponseEntity<PostResponseDto> - 조회된 게시글 정보를 포함한 응답 (HTTP 200 OK)
     */
    @GetMapping("/{postId}") // GET 요청으로 특정 게시글 조회
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getResponseDto(postId)); // HTTP 200 응답 반환
    }

    /**
     * 모든 게시글 조회 API (페이징 처리)
     *
     * 모든 게시글을 페이징 처리하여 조회합니다. 페이징 정보는 클라이언트가 요청으로 전달하며,
     * 기본적으로 한 페이지당 10개의 게시글이 작성일 기준 내림차순으로 조회됩니다.
     *
     * @param pageable 페이징 요청 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return ResponseEntity<Page<PostResponseDto>> - 페이징 처리된 게시글 정보를 포함한 응답 (HTTP 200 OK)
     */
    @GetMapping // GET 요청으로 모든 게시글 조회
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        // HTTP 200 응답 반환
        return ResponseEntity.ok(postService.getAll(pageable));
    }

    /**
     * 게시글 업데이트 API
     *
     * 특정 게시글의 ID와 업데이트할 제목 및 내용을 요청받아 게시글을 수정합니다.
     *
     * @param postId  업데이트할 게시글의 ID
     * @param request 업데이트할 제목과 내용을 포함한 요청 데이터
     * @return ResponseEntity<PostResponseDto> - 업데이트된 게시글 정보를 포함한 응답 (HTTP 200 OK)
     */
    @PutMapping("/{postId}") // PUT 요청으로 특정 게시글 업데이트
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                      @RequestBody @Valid PostRequestDto request) {
        // 서비스에서 제목과 내용을 업데이트
        PostResponseDto updatedResponse = postService.update(postId, request.getTitle(), request.getContent());
        return ResponseEntity.ok(updatedResponse); // HTTP 200 응답 반환
    }

    /**
     * 게시글 삭제 API
     *
     * 특정 게시글의 ID를 기반으로 게시글을 삭제합니다.
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


