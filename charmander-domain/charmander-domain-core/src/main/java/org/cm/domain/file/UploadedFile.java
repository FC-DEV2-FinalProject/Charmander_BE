package org.cm.domain.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.member.Member;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadedFile {
    @Id
    @Column(nullable = false, updatable = false, length = 512)
    private String id;

    @Column(updatable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false, insertable = false)
    private Member owner;

    @Convert(converter = UploadedFileType.Converter.class)
    @Column(nullable = false, updatable = false)
    private UploadedFileType uploadType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    UploadedFile(String fileId, UploadedFileType uploadType, Long ownerId) {
        this.id = fileId;
        this.uploadType = uploadType;
        this.ownerId = ownerId;
    }

    public static UploadedFile createUserUploadFile(String fileId, Long ownerId) {
        return new UploadedFile(fileId, UploadedFileType.USER_UPLOAD, ownerId);
    }
}
