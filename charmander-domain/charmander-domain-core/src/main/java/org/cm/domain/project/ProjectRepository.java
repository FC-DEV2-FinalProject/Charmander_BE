package org.cm.domain.project;

import jakarta.persistence.LockModeType;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwnerId(Long memberId);

    Optional<Project> findByIdAndOwnerId(Long id, Long memberId);

    @EntityGraph(attributePaths = {"scenes", "scenes.transcripts"})
    @Query("SELECT p FROM project p JOIN FETCH p.owner WHERE p.id = :id AND p.owner.id = :memberId")
    Optional<Project> findByIdAndOwnerIdWithFetch(Long id, Long memberId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM project p WHERE p.id = :id AND p.owner.id = :memberId")
    Optional<Project> findByIdAndOwnerIdForUpdate(Long id, Long memberId);

    @NonNull
    default Project getById(@NonNull Long projectId) {
        return findById(projectId)
            .orElseThrow(() -> new CoreDomainException(CoreDomainExceptionCode.NOT_FOUND_PROJECT));
    }

    // 네임드 get락
    @Transactional
    @Query(value = "SELECT GET_LOCK(:lockName, 2)", nativeQuery = true)
    Integer getLock(@Param("lockName") String lockName);

    // 네임드 release락
    @Transactional
    @Query(value = "SELECT RELEASE_LOCK(:lockName)", nativeQuery = true)
    Integer releaseLock(@Param("lockName") String lockName);

    // Project newsArticle 수정
    @Modifying
    @Query("UPDATE project p SET p.newsArticle = :newsArticle WHERE p.id = :id AND p.updatedAt < :updatedAt")
    int updateProjectNewsArticleFindById(@Param("id") Long id,
                                         @Param("newsArticle") String newsArticle,
                                         @Param("updatedAt")LocalDateTime localDateTime);
}
