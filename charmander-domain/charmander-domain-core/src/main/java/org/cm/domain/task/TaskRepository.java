package org.cm.domain.task;

import jakarta.persistence.LockModeType;
import java.util.List;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM task t WHERE t.project.owner.id = :memberId")
    List<Task> findByMemberId(Long memberId);

    @Query("SELECT t FROM task t WHERE t.id = :taskId AND t.project.owner.id = :memberId")
    Task findByIdAndMemberId(Long taskId, Long memberId);

    // TODO: 경합 가능성이 낮아고 생각해 낙관적 락으로 바꾸는걸 고려
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM task t WHERE t.id = :taskId AND t.project.owner.id = :memberId")
    Task findByIdAndMemberIdForUpdate(Long taskId, Long memberId);

    @NonNull
    default Task getById(@NonNull Long taskId) {
        return findById(taskId)
                .orElseThrow(() -> new CoreDomainException(CoreDomainExceptionCode.NOT_FOUND_TASK));
    }

    @Modifying
    @Transactional
    @Query("update task t set t.status = :status where t.id = :id")
    void update(@Param("id") Long id, @Param("status") TaskStatus status);

    @Modifying
    @Transactional
    @Query("update task t set t.status = :status, t.jobId = :jobId where t.id = :id")
    void update(
            @Param("id") Long id,
            @Param("status") TaskStatus status,
            @Param("jobId") String jobId
    );

    @Query("select t from task t where t.status = 'Converting' order by t.updatedAt asc limit :count")
    List<Task> findAllByConvertingTask(@Param("count") int count);

}
