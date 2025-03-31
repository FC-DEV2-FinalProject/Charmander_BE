package org.cm.domain.scene;

import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    @Query("""
            SELECT s
            FROM Scene s
            JOIN s.project p
            JOIN p.owner m
            WHERE s.id = :sceneId
              AND m.id = :memberId
        """)
    Optional<Scene> findByIdAndMemberId(@NonNull Long sceneId, @NonNull Long memberId);

    @Query("""
          SELECT s
          FROM Scene s
          JOIN s.project p
          JOIN p.owner m
          WHERE p.id = :projectId
            AND m.id = :memberId
      """)
    List<Scene> findProjectScenes(Long projectId, Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
          SELECT s
          FROM Scene s
          JOIN s.project p
          JOIN p.owner m
          WHERE p.id = :projectId
          AND s.id = :sceneId
          AND m.id = :memberId
      """)
    Optional<Scene> findProjectSceneForUpdate(Long projectId, Long sceneId, Long memberId);

    int countByProjectId(Long id);
}
