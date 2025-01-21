package org.example.restfulblogflatform.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 파일 업로드 관련 설정을 관리하는 설정 클래스
 * application.properties 또는 application.yml의 'file.upload' 프리픽스로 시작하는 설정값들을 바인딩
 *
 * 설정 예시 (application.yml):
 * file:
 *   upload:
 *     enabled: true
 *     location: /uploads
 *     max-file-size: 10MB
 *     max-request-size: 10MB
 *     path: /files
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "file.upload")
@Configuration
public class FileProperties {

    /**
     * 파일 업로드 기능 활성화 여부
     * true: 파일 업로드 허용
     * false: 파일 업로드 비활성화
     */
    private boolean enabled;

    /**
     * 파일이 실제로 저장될 서버의 물리적 경로
     * 예: "/home/app/uploads" 또는 "C:/uploads"
     *
     * 주의사항:
     * - 해당 경로에 대한 읽기/쓰기 권한이 있어야 함
     * - 경로가 존재하지 않을 경우 자동 생성됨
     */
    private String location;

    /**
     * 단일 파일의 최대 허용 크기
     * 형식: 숫자 + 단위(KB, MB)
     * 예: "10MB", "1024KB"
     *
     * 주의사항:
     * - 서버의 가용 메모리를 고려하여 설정
     * - Spring의 MultipartFile 설정과 연계 필요
     */
    private String maxFileSize;

    /**
     * 하나의 요청에서 처리할 수 있는 전체 파일 크기의 최대값
     * 형식: 숫자 + 단위(KB, MB)
     * 예: "20MB", "2048KB"
     *
     * 주의사항:
     * - 다중 파일 업로드 시 전체 크기 제한
     * - maxFileSize보다 크거나 같아야 함
     */
    private String maxRequestSize;

    /**
     * 파일 접근을 위한 URL 경로
     * 예: "/files", "/uploads", "/resources"
     *
     * 사용 예시:
     * - 웹 애플리케이션에서 파일 다운로드 URL 구성
     * - 정적 리소스 접근 경로 설정
     */
    private String path;
}
