package org.example.restfulblogflatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.comment.request.CommentRequestDto;
import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.security.CustomUserDetails;
import org.example.restfulblogflatform.service.comment.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글(Comment) 관련 REST API 컨트롤러.
 *
 * 댓글 작성, 조회와 같은 요청을 처리하는 컨트롤러입니다.
 * 인증된 사용자는 댓글을 작성할 수 있으며, 댓글 조회는 누구나 가능합니다.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성 API
     *
     * 특정 게시글에 댓글을 추가합니다. 인증된 사용자만 댓글 작성을 요청할 수 있습니다.
     *
     * @param postId      댓글을 작성할 게시글의 ID
     * @param requestDto  댓글 작성 요청 데이터 (댓글 내용 포함)
     * @param userDetails 인증된 사용자 정보 (현재 로그인된 사용자)
     * @return 생성된 댓글 정보와 HTTP 상태 코드 201 (Created)
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 댓글 생성 후 결과 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.add(postId, userDetails.getId(), requestDto.getContent()));
    }

    /**
     * 댓글 조회 API
     *
     * 특정 게시글에 달린 댓글 목록을 조회합니다. 조회된 데이터는 페이징 정보와 함께 반환됩니다.
     *
     * @param postId   댓글을 조회할 게시글의 ID
     * @param pageable 페이징 요청 정보 (페이지 번호, 크기, 정렬 기준 등)
     * @return 페이징 처리된 댓글 목록 (DTO 형태)
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Long postId,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {

        // 페이징 처리된 댓글 목록 반환
        return ResponseEntity.ok(commentService.getAll(postId, pageable));
    }
}
