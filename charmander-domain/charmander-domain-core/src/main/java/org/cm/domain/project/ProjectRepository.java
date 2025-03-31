package org.cm.domain.project;

import jakarta.persistence.LockModeType;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwnerId(Long memberId);

    Optional<Project> findByIdAndOwnerId(Long id, Long memberId);

    @Query("""
            SELECT p
            FROM project p
            JOIN FETCH p.owner
            LEFT OUTER JOIN FETCH p.scenes s
            LEFT OUTER JOIN FETCH s.transcripts t
            WHERE p.id = :id
              AND p.owner.id = :memberId
        """)
    Optional<Project> findByIdAndOwnerIdWithFetch(Long id, Long memberId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM project p WHERE p.id = :id AND p.owner.id = :memberId")
    Optional<Project> findByIdAndOwnerIdForUpdate(Long id, Long memberId);

    @NonNull
    default Project getById(@NonNull Long projectId) {
        return findById(projectId)
                .orElseThrow(() -> new CoreDomainException(CoreDomainExceptionCode.NOT_FOUND_PROJECT));
    }

    // Project newsArticle 수정
    @Modifying
    @Query("UPDATE project p SET p.newsArticle = :newsArticle WHERE p.id = :id AND p.updatedAt < :updatedAt")
    int updateProjectNewsArticleFindById(@Param("id") Long id,
                                         @Param("newsArticle") String newsArticle,
                                         @Param("updatedAt") LocalDateTime localDateTime);
}
