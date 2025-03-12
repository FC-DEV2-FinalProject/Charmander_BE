package org.cm.api.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cm.domain.task.Task;
import org.cm.domain.task.TaskRepository;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<Task> getMemberTasks(AuthInfo authInfo) {
        return taskRepository.findByMemberId(authInfo.getMemberId());
    }

    @Transactional(readOnly = true)
    public Task getMemberTask(Long taskId, AuthInfo authInfo) {
        return taskRepository.findByIdAndMemberId(taskId, authInfo.getMemberId());
    }

    public Task retryTask(Long taskId, AuthInfo authInfo) {
        var task = taskRepository.findByIdAndMemberIdForUpdate(taskId, authInfo.getMemberId());
        if (task == null) {
            throw new EntityNotFoundException("Task not found");
        }
        task.retry();
        return taskRepository.save(task);
    }
}
