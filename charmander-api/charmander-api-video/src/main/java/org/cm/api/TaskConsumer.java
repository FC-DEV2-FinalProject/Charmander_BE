package org.cm.api;


import lombok.RequiredArgsConstructor;
import org.cm.infra.storage.ContentsLocator;
import org.cm.infra.storage.PreSignedFileUploadService;
import org.cm.infra.storage.PreSignedURLIdentifier;
import org.cm.infra.storage.SingleFileUploadService;
import org.cm.service.TtsService;
import org.cm.vo.TaskCompletedCheckEvent;
import org.cm.vo.TaskScriptGenerationCompletedEvent;
import org.cm.vo.TaskScriptGenerationStartedEvent;
import org.cm.vo.TtsCreateCommand;
import org.cm.vo.TtsInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskConsumer {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final SingleFileUploadService singleFileUploadService;
    private final ContentsLocator contentsLocator;
    private final TtsService ttsService;
    private final PreSignedFileUploadService preSignedFileUploadService;

    @Value("${consumer.host}")
    private String serverURL;

    @KafkaListener(topics = "tts-task", groupId = "tts-task-group")
    public void consume(TaskScriptRecord taskScriptRecord) {
        prepareTaskScriptGeneration(taskScriptRecord);

        var preSignedURLIdentifier = preSignedFileUploadService.sign(contentsLocator);
        var ttsInfo = createTts(taskScriptRecord, preSignedURLIdentifier);

        completeTaskScriptGeneration(taskScriptRecord, preSignedURLIdentifier, ttsInfo);
    }

    private void completeTaskScriptGeneration(
            TaskScriptRecord taskScriptRecord,
            PreSignedURLIdentifier preSignedURLIdentifier,
            TtsInfo ttsInfo
    ) {
        var taskScriptGenerationCompletedEvent = new TaskScriptGenerationCompletedEvent(
                taskScriptRecord.taskId(),
                taskScriptRecord.taskScriptId(),
                preSignedURLIdentifier.fileName(),
                ttsInfo.playTime()
        );
        applicationEventPublisher.publishEvent(taskScriptGenerationCompletedEvent);
        applicationEventPublisher.publishEvent(new TaskCompletedCheckEvent(taskScriptRecord.taskId()));
    }

    private TtsInfo createTts(
            TaskScriptRecord taskScriptRecord,
            PreSignedURLIdentifier preSignedURLIdentifier
    ) {
        var ttsCreateCommand = new TtsCreateCommand(
                taskScriptRecord.sentence(),
                taskScriptRecord.option(),
                serverURL,
                preSignedURLIdentifier.fullPath(),
                preSignedURLIdentifier.fileName(),
                preSignedURLIdentifier.uploadId()
        );
        return ttsService.create(ttsCreateCommand);
    }

    private void prepareTaskScriptGeneration(TaskScriptRecord taskScriptRecord) {
        var taskScriptGenerationStartedEvent = new TaskScriptGenerationStartedEvent(
                taskScriptRecord.taskId(),
                taskScriptRecord.taskScriptId()
        );
        applicationEventPublisher.publishEvent(taskScriptGenerationStartedEvent);
    }
}
