package org.cm.domain.scene;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    @Query(
        "SELECT s FROM Scene s " +
        "JOIN s.project p " +
        "JOIN p.owner m " +
        "WHERE p.id = :projectId " +
        "AND m.id = :memberId"
    )
    List<Scene> findProjectScenes(Long projectId, Long memberId);
}
