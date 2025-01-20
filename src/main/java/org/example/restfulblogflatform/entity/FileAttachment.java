package org.example.restfulblogflatform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_attachments")
public class FileAttachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

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

    public void setPost(Post post) {
        this.post = post;
    }
}
