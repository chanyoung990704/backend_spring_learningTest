package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 파일 첨부 정보를 관리하는 엔티티 클래스
 * 게시글(Post)에 첨부되는 파일의 메타데이터를 저장합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_attachments")
public class FileAttachment extends BaseEntity {

    /**
     * 파일 첨부 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자가 업로드한 원본 파일명
     */
    @Column(nullable = false)
    private String originalFileName;

    /**
     * 서버에 저장된 고유한 파일명
     */
    @Column(nullable = false)
    private String storedFileName;

    /**
     * 파일이 저장된 서버 내 경로
     */
    @Column(nullable = false)
    private String filePath;

    /**
     * 파일 크기 (바이트 단위)
     */
    @Column(nullable = false)
    private Long fileSize;

    /**
     * 파일의 MIME 타입
     */
    @Column(nullable = false)
    private String fileType;

    /**
     * 게시글(Post)과의 다대일 관계 설정
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 파일 첨부 객체 생성 메서드
     *
     * @param originalFileName 원본 파일명
     * @param storedFileName 저장된 파일명
     * @param filePath 파일 저장 경로
     * @param fileSize 파일 크기
     * @param fileType 파일 타입
     * @param post 연관된 게시글 객체
     * @return 생성된 FileAttachment 객체
     */
    public static FileAttachment createFileAttachment(String originalFileName,
                                                      String storedFileName,
                                                      String filePath,
                                                      Long fileSize,
                                                      String fileType,
                                                      Post post) {
        FileAttachment attachment = new FileAttachment();
        attachment.originalFileName = originalFileName;
        attachment.storedFileName = storedFileName;
        attachment.filePath = filePath;
        attachment.fileSize = fileSize;
        attachment.fileType = fileType;
        attachment.post = post;
        return attachment;
    }

    /**
     * 연관된 게시글을 설정하는 메서드 (양방향 관계 설정 시 사용)
     *
     * @param post 게시글 객체
     */
    public void setPost(Post post) {
        this.post = post;
    }
}

