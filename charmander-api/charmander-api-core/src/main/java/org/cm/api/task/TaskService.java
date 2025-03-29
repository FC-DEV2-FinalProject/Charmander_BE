package org.cm.api.task;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cm.api.task.event.TaskCreationEvent;
import org.cm.domain.project.Project;
import org.cm.domain.scene.Scene;
import org.cm.domain.task.Task;
import org.cm.domain.task.TaskRepository;
import org.cm.domain.task.TaskType;
import org.cm.domain.taskscript.TaskScriptFactory;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.security.AuthInfo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService implements ApplicationEventPublisherAware {
    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

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
            throw new CoreApiException(CoreApiExceptionCode.TASK_NOT_FOUND);
        }
        task.retry();
        return taskRepository.save(task);
    }

    public Task createTask(Project project, TaskType taskType) {
        var task = new Task(project, taskType);
        var taskScripts = project.getScenes()
            .stream()
            .map(Scene::getTranscripts)
            .flatMap(Collection::stream)
            .map((ts) -> TaskScriptFactory.create(task, ts))
            .toList();
        task.setTaskScripts(taskScripts);
        taskRepository.save(task);
        applicationEventPublisher.publishEvent(new TaskCreationEvent(task));
        return task;
    }
}
