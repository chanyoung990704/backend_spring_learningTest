package org.example.restfulblogflatform.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.restfulblogflatform.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "log_entries")
public class LogEntry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false)
    private String level; // 로그 레벨 (e.g., INFO, ERROR)

    @Column(nullable = false)
    private String message; // 로그 메시지

    @Column(nullable = true)
    private String exception; // 예외 메시지 (옵션)
}
