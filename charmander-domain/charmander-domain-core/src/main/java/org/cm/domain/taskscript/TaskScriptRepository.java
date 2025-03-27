package org.cm.domain.taskscript;

import java.util.List;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskScriptRepository extends JpaRepository<TaskScript, Long> {

    @NonNull
    default TaskScript getById(@NonNull Long id) {
        return findById(id)
                .orElseThrow(() -> new CoreDomainException(CoreDomainExceptionCode.NOT_FOUND_TASK_SCRIPT));
    }

    @Query("SELECT COUNT(t) FROM TaskScript t WHERE t.task.id = :taskId AND t.status != 'Success'")
    long countNotSuccessTaskScriptsByTaskId(@Param("taskId") Long taskId);

    List<TaskScript> findAllByTaskId(Long taskId);

    @Query("select t from TaskScript t where t.sceneId = :sceneId and t.status != 'Success'")
    List<TaskScript> findAllNotSuccessTaskScriptsBySceneId(Long sceneId);
}
