package org.cm.domain.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.member.Member;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadedFile {
    @Id
    @Column(nullable = false, updatable = false, length = 512)
    private String id;

    @Nullable
    @Column(name = "owner_id", updatable = false)
    private Long ownerId;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", updatable = false, insertable = false)
    private Member owner;

    @Convert(converter = UploadedFileType.Converter.class)
    @Column(nullable = false, updatable = false)
    private UploadedFileType uploadType;

    @Convert(converter = UploadedFileStatus.Converter.class)
    @Column(nullable = false)
    private UploadedFileStatus status = UploadedFileStatus.UPLOADING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    UploadedFile(String fileId, UploadedFileType uploadType, Long ownerId) {
        this.id = fileId;
        this.uploadType = uploadType;
        this.ownerId = ownerId;
    }

    public boolean hasOwnership(Long memberId) {
        return Objects.equals(ownerId, memberId);
    }

    public void startCompletion() {
        if (status != UploadedFileStatus.UPLOADING) {
            throw new CoreDomainException(CoreDomainExceptionCode.INVALID_FILE_STATUS);
        }
        status = UploadedFileStatus.COMPLETE_IN_PROGRESS;
    }

    public void finishCompletion() {
        if (status != UploadedFileStatus.COMPLETE_IN_PROGRESS) {
            throw new CoreDomainException(CoreDomainExceptionCode.INVALID_FILE_STATUS);
        }
        status = UploadedFileStatus.COMPLETED;
    }

    public static UploadedFile createUserUploadFile(String fileId, Long ownerId) {
        return new UploadedFile(fileId, UploadedFileType.USER_UPLOAD, ownerId);
    }
}
