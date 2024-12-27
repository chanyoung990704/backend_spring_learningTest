package org.example.restfulblogflatform.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.restfulblogflatform.entity.BaseEntity;

/**
 * 로그(Log) 데이터를 저장하기 위한 엔티티 클래스.
 * 애플리케이션에서 발생하는 로그 정보를 데이터베이스에 저장합니다.
 */
@Getter // Lombok 어노테이션: 각 필드에 대한 Getter 메서드를 자동 생성
@Setter // Lombok 어노테이션: 각 필드에 대한 Setter 메서드를 자동 생성
@Entity // JPA 엔티티로 지정
@Table(name = "log_entries") // 데이터베이스 테이블 이름을 "log_entries"로 지정
public class LogEntry extends BaseEntity { // BaseEntity를 상속받아 생성/수정 시간 관리

    @Id // 기본 키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 증가(AUTO_INCREMENT) 방식으로 설정
    private Long id; // 로그 엔트리의 고유 식별자

    @Column(nullable = false) // null 불가
    private String level; // 로그 레벨 (e.g., INFO, ERROR, WARN)

    @Column(nullable = false, columnDefinition = "TEXT") // null 불가, TEXT 타입으로 저장
    private String message; // 로그 메시지

    @Column(nullable = true, columnDefinition = "TEXT") // null 허용, TEXT 타입으로 저장
    private String exception; // 예외 메시지 (선택적 필드)
}