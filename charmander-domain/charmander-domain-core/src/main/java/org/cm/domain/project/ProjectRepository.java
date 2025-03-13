package org.cm.domain.project;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwnerId(Long memberId);

    Optional<Project> findByIdAndOwnerId(Long id, Long memberId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM project p WHERE p.id = :id AND p.owner.id = :memberId")
    Optional<Project> findByIdAndOwnerIdForUpdate(Long id, Long memberId);
}
