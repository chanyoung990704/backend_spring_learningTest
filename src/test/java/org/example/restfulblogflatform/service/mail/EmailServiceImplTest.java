package org.example.restfulblogflatform.service.mail;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmailServiceImplTest {

    // Mock 객체 생성: JavaMailSender는 실제 구현체 대신 Mockito를 사용하여 Mock으로 생성합니다.
    private final JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);

    // EmailServiceImpl 인스턴스 생성: 테스트 대상 클래스에 Mock된 JavaMailSender를 주입합니다.
    private final EmailServiceImpl emailService = new EmailServiceImpl(mailSender);

    @Test
    void testSendEmail() {
        // Given: 테스트에 필요한 데이터를 준비합니다.
        String to = "test@example.com"; // 이메일 수신자
        String subject = "Test Subject"; // 이메일 제목
        String body = "Test Body"; // 이메일 본문

        // When: 테스트 대상 메서드를 호출합니다.
        emailService.sendEmail(to, subject, body);

        // Then: 메서드 호출 결과를 검증합니다.

        // ArgumentCaptor를 사용하여 send 메서드에 전달된 SimpleMailMessage 객체를 캡처합니다.
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // JavaMailSender의 send 메서드가 정확히 한 번 호출되었는지 검증합니다.
        verify(mailSender, times(1)).send(messageCaptor.capture());

        // 캡처된 SimpleMailMessage 객체를 가져옵니다.
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        // 캡처된 메시지의 수신자(to), 제목(subject), 본문(body)이 기대값과 일치하는지 검증합니다.
        assertEquals(to, sentMessage.getTo()[0]); // 수신자가 올바른지 확인
        assertEquals(subject, sentMessage.getSubject()); // 제목이 올바른지 확인
        assertEquals(body, sentMessage.getText()); // 본문이 올바른지 확인
    }
}