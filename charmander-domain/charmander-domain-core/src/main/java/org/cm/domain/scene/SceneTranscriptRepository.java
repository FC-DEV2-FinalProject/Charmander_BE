package org.cm.domain.scene;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SceneTranscriptRepository extends JpaRepository<SceneTranscript, Long> {
    @Query("""
         SELECT st
         FROM SceneTranscript st
         WHERE st.id = :tsId
           AND st.scene.id = :sceneId
           ANd st.scene.project.id = :projectId
           AND st.scene.project.owner.id = :memberId
        """)
    Optional<SceneTranscript> findForUpdate(Long projectId, Long sceneId, Long tsId, Long memberId);
}
