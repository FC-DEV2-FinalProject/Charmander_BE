package org.cm.domain.task;

import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SceneOutputRepository extends JpaRepository<SceneOutput, Long> {

    Optional<SceneOutput> findByTaskIdAndSceneId(Long taskId, Long sceneId);

    @NonNull
    default SceneOutput getById(@NonNull Long taskId) {
        return findById(taskId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
