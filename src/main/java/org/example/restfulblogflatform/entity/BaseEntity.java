package org.example.restfulblogflatform.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 엔티티의 공통 속성을 정의하는 추상 클래스.
 * 모든 엔티티에서 생성 시간과 수정 시간을 자동으로 관리하기 위해 사용됩니다.
 */
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
@MappedSuperclass // JPA 어노테이션: 이 클래스를 상속받는 엔티티가 필드를 공유하도록 설정
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 기능 활성화 (엔티티의 변경 사항을 감지)
public class BaseEntity {

    /**
     * 엔티티가 생성된 시간.
     * JPA Auditing에 의해 자동으로 값이 설정됩니다.
     */
    @CreatedDate // 엔티티가 처음 생성될 때의 시간을 자동으로 기록
    private LocalDateTime createdDate;

    /**
     * 엔티티가 마지막으로 수정된 시간.
     * JPA Auditing에 의해 자동으로 값이 업데이트됩니다.
     */
    @LastModifiedDate // 엔티티가 수정될 때의 시간을 자동으로 기록
    private LocalDateTime lastModifiedDate;
}
