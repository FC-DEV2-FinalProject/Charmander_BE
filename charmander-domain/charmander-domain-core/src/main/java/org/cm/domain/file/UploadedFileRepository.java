package org.cm.domain.file;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, String> {
    Page<UploadedFile> findByOwner_Id(Long ownerId, Pageable pageable);

    // TODO: [튜닝] DB에 직접 UPDATE 쿼리를 날리는 방식과 비교?
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT f FROM UploadedFile f WHERE f.id = :s")
    Optional<UploadedFile> findByIdForUpdate(String s);
}
