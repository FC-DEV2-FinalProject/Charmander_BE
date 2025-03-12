package org.cm.api.task;

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

    public List<Task> getMemberTasks(AuthInfo authInfo) {
        return taskRepository.findByMemberId(authInfo.getMemberId());
    }

    public Task getMemberTask(Long taskId, AuthInfo authInfo) {
        return taskRepository.findByIdAndMemberId(taskId, authInfo.getMemberId());
    }
}
