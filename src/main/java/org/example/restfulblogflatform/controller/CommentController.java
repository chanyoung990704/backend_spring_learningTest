package org.example.restfulblogflatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.comment.request.CommentRequestDto;
import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.security.CustomUserDetails;
import org.example.restfulblogflatform.service.comment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RESTful API 컨트롤러: 댓글(Comment) 관련 요청 처리.
 *
 * 이 컨트롤러는 게시글에 댓글을 생성하거나 조회하는 API를 제공합니다.
 * 인증된 사용자만 댓글을 작성할 수 있으며, 댓글 조회는 누구나 가능합니다.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성 API
     *
     * 이 메서드는 특정 게시글에 댓글을 추가합니다.
     * 인증된 사용자만 댓글을 작성할 수 있습니다.
     *
     * @param postId 게시글의 ID (댓글이 달릴 대상 게시글)
     * @param requestDto 댓글 생성 요청 DTO (content 필드 포함)
     * @param userDetails 현재 인증된 사용자 정보 (Spring Security에서 제공)
     * @return 생성된 댓글의 응답 DTO와 HTTP 상태 코드 201 (Created)
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long postId,
                                                         @RequestBody CommentRequestDto requestDto,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 댓글 생성 로직 호출
        Comment createdComment = commentService.add(postId, userDetails.getId(), requestDto.getContent());

        // 생성된 댓글 정보를 응답 DTO로 변환하여 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponseDto.of(createdComment));
    }

    /**
     * 댓글 조회 API
     *
     * 이 메서드는 특정 게시글에 달린 모든 댓글을 조회합니다.
     * 게시글 ID를 기준으로 해당 게시글의 모든 댓글을 반환합니다.
     *
     * @param postId 게시글의 ID (댓글이 달린 대상 게시글)
     * @return 해당 게시글의 모든 댓글 리스트 (응답 DTO 형태)
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        // 특정 게시글에 대한 모든 댓글 조회 로직 호출
        List<CommentResponseDto> comments = commentService.getAll(postId);

        // 조회된 댓글 리스트 반환
        return ResponseEntity.ok(comments);
    }
}