package org.cm.service;

import lombok.RequiredArgsConstructor;
import org.cm.domain.task.TaskRepository;
import org.cm.vo.TtsCreateCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TtsService ttsService;

    public void start(Long taskId) {
        var task = taskRepository.getById(taskId);

        task.start();

        ttsService.create(new TtsCreateCommand(null));
    }

}
