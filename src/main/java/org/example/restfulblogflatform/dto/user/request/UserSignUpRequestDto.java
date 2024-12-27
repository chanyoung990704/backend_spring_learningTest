package org.example.restfulblogflatform.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

/**
 * 회원가입 요청 데이터를 담는 DTO(Data Transfer Object).
 * 클라이언트가 전달한 이메일, 비밀번호, 이름 정보를 포함하며, 유효성 검증을 수행합니다.
 */
@Data // Lombok 어노테이션: @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 포함
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드 자동 생성
@Setter // Lombok 어노테이션: 각 필드에 대한 Setter 메서드 자동 생성
@AllArgsConstructor // Lombok 어노테이션: 모든 필드를 포함하는 생성자를 자동 생성
public class UserSignUpRequestDto implements Serializable { // 직렬화를 지원하는 클래스

    @NotBlank(message = "이메일은 필수 입력값입니다.") // 이메일이 비어있거나 공백일 경우 유효성 검증 실패
    @Email(message = "이메일 형식이 올바르지 않습니다.") // 이메일 형식 유효성 검증
    private String email; // 사용자 이메일

    @NotBlank(message = "비밀번호는 필수 입력값입니다.") // 비밀번호가 비어있거나 공백일 경우 유효성 검증 실패
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다."
    ) // 비밀번호 형식 유효성 검증 (최소 8자, 영문, 숫자, 특수문자 포함)
    private String password; // 사용자 비밀번호

    @NotBlank(message = "이름은 필수 입력값입니다.") // 이름이 비어있거나 공백일 경우 유효성 검증 실패
    private String username; // 사용자 이름
}