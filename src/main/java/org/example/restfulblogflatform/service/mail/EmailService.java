package org.example.restfulblogflatform.service.mail;

/**
 * 이메일 전송 서비스 인터페이스
 *
 * 이메일 전송 기능을 정의하는 인터페이스로, 구현체를 통해 다양한 이메일 전송 방식을 지원할 수 있습니다.
 * 예를 들어, SMTP, 외부 이메일 API 등을 활용한 구현이 가능합니다.
 */
public interface EmailService {

    /**
     * 이메일을 전송합니다.
     *
     * @param to      수신자 이메일 주소
     * @param subject 이메일 제목
     * @param body    이메일 본문
     */
    public void sendEmail(String to, String subject, String body);

}
