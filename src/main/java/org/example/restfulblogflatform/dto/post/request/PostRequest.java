package org.example.restfulblogflatform.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 게시글 생성 및 수정 요청 데이터를 담는 DTO(Data Transfer Object).
 * 클라이언트가 전달하는 게시글 제목과 내용을 포함하며, 유효성 검증을 수행합니다.
 */
@Data // Lombok 어노테이션: @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 포함
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드 자동 생성
@Setter // Lombok 어노테이션: 각 필드에 대한 Setter 메서드 자동 생성
@AllArgsConstructor // Lombok 어노테이션: 모든 필드를 포함하는 생성자 자동 생성
public class PostRequest {

    @NotBlank(message = "제목은 필수 입니다.") // 제목이 비어있거나 공백일 경우 유효성 검증 실패
    private String title; // 게시글 제목

    @NotBlank(message = "내용은 필수입니다.") // 내용이 비어있거나 공백일 경우 유효성 검증 실패
    private String content; // 게시글 내용
}
