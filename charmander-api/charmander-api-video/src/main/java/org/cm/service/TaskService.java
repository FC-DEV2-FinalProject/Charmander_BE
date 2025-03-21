package org.cm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.domain.task.TaskRepository;
import org.cm.domain.task.TaskStatus;
import org.cm.domain.taskscript.TaskScriptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskScriptRepository taskScriptRepository;

    public void tryConvert(Long taskId) {
        // 모든 작업이 완료된 경우
        if (taskScriptRepository.countNotSuccessTaskScriptsByTaskId(taskId) != 0) {
            return;
        }
        taskRepository.update(taskId, TaskStatus.CONVERTING);
        // to media convert
        // taskScriptRepository.findAllByTaskId(taskId);
        // TODO media convert 업로드 기능 추가
    }
}
