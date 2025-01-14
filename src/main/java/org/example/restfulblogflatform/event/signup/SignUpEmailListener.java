package org.example.restfulblogflatform.event.signup;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.service.mail.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 회원가입 이메일 알림을 처리하는 이벤트 리스너
 *
 * @Component: Spring Bean으로 등록
 * @RequiredArgsConstructor: final 필드를 포함한 생성자 자동 생성
 */
@Component
@RequiredArgsConstructor
public class SignUpEmailListener {

    private final EmailService emailService; // 이메일 전송 서비스

    /**
     * 회원가입 이벤트를 처리합니다.
     *
     * @EventListener: 특정 이벤트 발생 시 실행되는 메서드로 등록
     * @param event 회원가입 이메일 이벤트(SignUpEmailEvent)
     */
    @EventListener
    public void handleSignUpEmailEvent(SignUpEmailEvent event) {
        // 이메일 제목 및 본문 생성
        String subject = "회원이 되신 것을 환영합니다!";
        String body = event.getUsername() + "님, 회원가입이 완료되었습니다.";

        // 이메일 전송 서비스 호출
        emailService.sendEmail(event.getEmail(), subject, body);
    }
}