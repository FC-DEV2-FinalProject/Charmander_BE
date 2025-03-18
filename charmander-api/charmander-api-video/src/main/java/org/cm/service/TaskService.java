package org.cm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.domain.task.TaskOutput;
import org.cm.domain.task.TaskRepository;
import org.cm.infra.storage.ContentsLocator;
import org.cm.infra.storage.PreSignedFileUploadService;
import org.cm.infra.storage.PreSignedURLAbortCommand;
import org.cm.infra.storage.PreSignedURLCompleteCommand;
import org.cm.vo.TtsCreateCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final PreSignedFileUploadService fileUploadService;
    private final ContentsLocator contentsLocator;
    private final TtsService ttsService;

    // TODO 트랜잭션에 포함시킬까?
    public void start(Long taskId) {
        var task = taskRepository.getById(taskId);

        task.start();

        var preSignedURLIdentifier = fileUploadService.sign(contentsLocator);

        ttsService.create(new TtsCreateCommand(taskId, preSignedURLIdentifier, null));
    }


    // TODO 이벤트 분리 예정
    public void complete(Long taskId, TaskOutput output, PreSignedURLCompleteCommand command) {
        var task = taskRepository.getById(taskId);

        fileUploadService.complete(contentsLocator, command);

        task.succeed(output);
    }

    // TODO 분리할지 고민 중
    public void abort(Long taskId, PreSignedURLAbortCommand command){
        var task = taskRepository.getById(taskId);

        fileUploadService.abort(contentsLocator, command);

        task.cancel();
    }


}
