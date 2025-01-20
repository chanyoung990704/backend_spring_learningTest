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
 * 게시글(Post) 관련 요청을 처리하는 REST 컨트롤러.
 *
 * 이 컨트롤러는 게시글 생성, 조회, 수정, 삭제와 같은 CRUD(생성, 읽기, 갱신, 삭제) 기능을 제공합니다.
 * 클라이언트 요청을 처리하고, 적절한 응답을 반환합니다.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PagedResourcesAssembler<PostResponseDto> pagedResourcesAssembler;

    /**
     * 게시글 생성 API (파일 업로드 포함)
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostResponseDto> createPost(
            @ModelAttribute @Valid PostRequestDto request,  // @ModelAttribute로 변경
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostResponseDto response = postService.add(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 기존 메서드들은 동일하게 유지
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getResponseDto(postId));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PostResponseDto>>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PostResponseDto> posts = postService.getAll(pageable);
        PagedModel<EntityModel<PostResponseDto>> pagedModel =
                pagedResourcesAssembler.toModel(posts);
        return ResponseEntity.ok(pagedModel);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}

