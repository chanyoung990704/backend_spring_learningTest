package org.example.restfulblogflatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.dto.comment.request.CommentRequestDto;
import org.example.restfulblogflatform.dto.comment.response.CommentResponseDto;
import org.example.restfulblogflatform.entity.Comment;
import org.example.restfulblogflatform.service.comment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RESTful API 컨트롤러: 댓글(Comment) 관련 요청 처리.
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글을 생성합니다.
     *
     * @param postId 댓글이 달릴 게시글의 ID
     * @param requestDto 댓글 생성 요청 DTO (userId, content 포함)
     * @return 생성된 댓글의 응답 DTO와 HTTP 상태 코드 201 (Created)
     */
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long postId,
                                                         @RequestBody CommentRequestDto requestDto) {
        // 댓글 생성
        Comment createdComment = commentService.add(postId, requestDto.getUserId(), requestDto.getContent());

        // 정적 팩토리 메서드를 사용해 응답 DTO 생성
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponseDto.of(createdComment));
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 ID
     * @return HTTP 상태 코드 204 (No Content)
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }}