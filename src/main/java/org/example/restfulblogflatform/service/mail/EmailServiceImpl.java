package org.example.restfulblogflatform.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 이메일 전송 서비스 구현체
 *
 * Spring의 JavaMailSender를 사용하여 이메일을 전송하는 구현 클래스입니다.
 *
 * @Service: Spring의 Service 계층으로 등록
 * @RequiredArgsConstructor: final 필드를 포함한 생성자를 자동 생성
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender; // Spring에서 제공하는 이메일 전송 도구

    /**
     * 이메일을 전송합니다.
     *
     * @param to      수신자 이메일 주소
     * @param subject 이메일 제목
     * @param body    이메일 본문
     */
    @Override
    public void sendEmail(String to, String subject, String body) {
        // SimpleMailMessage 객체를 생성하여 이메일 데이터를 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);            // 수신자 설정
        message.setSubject(subject);  // 제목 설정
        message.setText(body);        // 본문 설정

        // JavaMailSender를 사용하여 이메일 전송
        mailSender.send(message);
    }
}