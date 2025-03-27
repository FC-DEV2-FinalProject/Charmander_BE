package org.cm.domain.task;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SceneOutputRepository extends JpaRepository<SceneOutput, Long> {

    Optional<SceneOutput> findByTaskIdAndSceneId(Long taskId, Long sceneId);

    List<SceneOutput> findAllByTaskId(Long taskId);

    @NonNull
    default SceneOutput getById(@NonNull Long taskId) {
        return findById(taskId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
