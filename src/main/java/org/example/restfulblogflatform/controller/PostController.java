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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 게시글(Post) 관련 요청을 처리하는 REST 컨트롤러
 *
 * 게시글의 CRUD 작업과 파일 첨부 기능을 제공하는 RESTful API 엔드포인트들을 정의합니다.
 * 모든 응답은 HTTP 표준 상태 코드를 준수합니다.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PagedResourcesAssembler<PostResponseDto> pagedResourcesAssembler;

    /**
     * 새로운 게시글을 생성하는 엔드포인트
     *
     * @param request 게시글 생성 요청 데이터 (제목, 내용, 첨부파일 등)
     * @param userDetails 인증된 사용자 정보
     * @return 생성된 게시글 정보와 HTTP 201 Created 상태
     *
     * 요청 예시:
     * POST /api/posts
     * Content-Type: multipart/form-data
     *
     * - title: 게시글 제목
     * - content: 게시글 내용
     * - files: 첨부파일들 (선택적)
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostResponseDto> createPost(
            @ModelAttribute @Valid PostRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostResponseDto response = postService.add(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 특정 게시글을 조회하는 엔드포인트
     *
     * @param postId 조회할 게시글의 고유 식별자
     * @return 조회된 게시글 정보와 HTTP 200 OK 상태

     * 요청 예시:
     * GET /api/posts/{postId}
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getResponseDto(postId));
    }

    /**
     * 게시글 목록을 페이징하여 조회하는 엔드포인트
     *
     * @param pageable 페이징 정보 (페이지 번호, 크기, 정렬 기준)
     * @return 페이징된 게시글 목록과 HTTP 200 OK 상태
     *
     * 기본 설정:
     * - 페이지 크기: 10개
     * - 정렬: 생성일 기준 내림차순
     *
     * 요청 예시:
     * GET /api/posts?page=0&size=10&sort=createdDate,desc
     */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PostResponseDto>>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PostResponseDto> posts = postService.getAll(pageable);
        PagedModel<EntityModel<PostResponseDto>> pagedModel =
                pagedResourcesAssembler.toModel(posts);
        return ResponseEntity.ok(pagedModel);
    }

    /**
     * 특정 게시글을 삭제하는 엔드포인트
     *
     * @param postId 삭제할 게시글의 고유 식별자
     * @return HTTP 204 No Content 상태
     *
     *
     * 요청 예시:
     * DELETE /api/posts/{postId}
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}


