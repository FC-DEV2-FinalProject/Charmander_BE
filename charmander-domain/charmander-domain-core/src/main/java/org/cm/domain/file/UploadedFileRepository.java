package org.cm.domain.file;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, String> {
    @Query("SELECT f FROM UploadedFile f WHERE f.ownerId = :ownerId AND f.status = :status")
    Page<UploadedFile> findByOwnerIdAndStatus(Long ownerId, UploadedFileStatus status, Pageable pageable);

    @Query("SELECT f FROM UploadedFile f WHERE f.fullPath = :fileId AND f.ownerId = :memberId AND f.status = :status")
    Optional<UploadedFile> findByFullPathAndOwnerIdAndStatus(String fileId, Long memberId, UploadedFileStatus status);

    // TODO: [튜닝] DB에 직접 UPDATE 쿼리를 날리는 방식과 비교?
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT f FROM UploadedFile f WHERE f.uploadId = :uploadId")
    Optional<UploadedFile> findByUploadIdForUpdate(String uploadId);
}
