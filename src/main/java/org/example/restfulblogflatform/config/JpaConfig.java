package org.example.restfulblogflatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 설정을 위한 구성 클래스.
 * 이 클래스는 JPA Auditing 기능을 활성화하여 엔티티의 생성 시간, 수정 시간 등을
 * 자동으로 관리할 수 있도록 설정합니다.
 */
@Configuration // Spring에서 설정 클래스로 인식되도록 지정
@EnableJpaAuditing // JPA Auditing 기능 활성화 (예: @CreatedDate, @LastModifiedDate 사용 가능)
public class JpaConfig {
    // 별도의 설정이 필요하지 않으므로 클래스만 정의
}

