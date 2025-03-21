package org.cm.service;

import lombok.RequiredArgsConstructor;
import org.cm.domain.taskscript.TaskScriptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskScriptService {

    private final TaskScriptRepository taskScriptRepository;

    // TODO 롤백 처리를 어떻게 할지 고민할 것
    @Transactional
    public void start(Long taskId) {
        var taskScript = taskScriptRepository.getById(taskId);

        taskScript.start();
    }

    @Transactional
    public void complete(Long taskScriptId, String fileId) {
        // TODO CANCEL 어떻게 처리할지
        var taskScript = taskScriptRepository.getById(taskScriptId);
        taskScript.complete(fileId);
    }
}
