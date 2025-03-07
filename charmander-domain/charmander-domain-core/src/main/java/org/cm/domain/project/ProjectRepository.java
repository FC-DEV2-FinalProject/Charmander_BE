package org.cm.domain.project;

import jakarta.persistence.LockModeType;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwnerId(Long memberId);

    @Nullable
    Project findByIdAndOwnerId(Long id, Long memberId);

    @Nullable
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM project p WHERE p.id = :id AND p.owner.id = :memberId")
    Project findByIdAndOwnerIdForUpdate(Long id, Long memberId);
}
