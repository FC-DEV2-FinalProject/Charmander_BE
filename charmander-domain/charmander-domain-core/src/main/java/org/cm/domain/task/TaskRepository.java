package org.cm.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM task t WHERE t.project.owner.id = :memberId")
    List<Task> findByMemberId(Long memberId);

    @Query("SELECT t FROM task t WHERE t.id = :taskId AND t.project.owner.id = :memberId")
    Task findByIdAndMemberId(Long taskId, Long memberId);
}
