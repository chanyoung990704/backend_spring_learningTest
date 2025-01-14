package org.example.restfulblogflatform.event.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 이메일 이벤트 클래스
 *
 * 회원가입이 완료된 후 이메일 알림을 위해 사용되는 이벤트 객체입니다.
 * Spring의 이벤트 기반 비동기 처리를 위한 데이터 전달 역할을 합니다.
 *
 * @Getter: 모든 필드에 대한 Getter 메서드를 자동 생성
 * @AllArgsConstructor: 모든 필드를 포함하는 생성자를 자동 생성
 */
@Getter
@AllArgsConstructor
public class SignUpEmailEvent {

    private final String email;       // 수신 이메일 주소 (회원가입 사용자의 이메일)
    private final String username;    // 사용자 이름 (회원가입 사용자의 이름)
}
