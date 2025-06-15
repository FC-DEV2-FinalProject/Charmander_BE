package org.cm.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.common.domain.FileType;
import org.cm.infra.storage.ContentsLocator;
import org.cm.infra.storage.PreSignedFileUploadService;
import org.cm.infra.storage.PreSignedURLIdentifier;
import org.cm.kafka.KafkaTopicNames;
import org.cm.kafka.TaskScriptRecord;
import org.cm.service.TaskScriptService;
import org.cm.service.TtsService;
import org.cm.vo.TtsCombineEvent;
import org.cm.vo.TtsCreateCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskConsumer {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ContentsLocator contentsLocator;
    private final TtsService ttsService;
    private final PreSignedFileUploadService preSignedFileUploadService;
    private final TaskScriptService taskScriptService;

    @Value("${consumer.host}")
    private String serverURL;

    @KafkaListener(topics = KafkaTopicNames.TTS_PROCESSING, groupId = "tts-task-group")
    public void consume(TaskScriptRecord taskScriptRecord) {
        try {
            subExecute(taskScriptRecord);
        } catch (Exception e) {
            // TODO 예외에 따른 복구 및 재처리 전략 구성하기
            log.error("{}", e.getMessage(), e);
        }

    }

    private void subExecute(TaskScriptRecord taskScriptRecord) {
        // 스크립트 작업 시작 (1)
        taskScriptService.start(taskScriptRecord.taskScriptId());

        // TTS 파일 생성
        var preSignedURLIdentifier = generateTts(taskScriptRecord);

        // 스크립트 작업 완료
        taskScriptService.complete(taskScriptRecord.taskScriptId(), preSignedURLIdentifier.fileName());

        // 스크립트 (검사 & 결합)
        applicationEventPublisher.publishEvent(new TtsCombineEvent(
                taskScriptRecord.taskId(),
                taskScriptRecord.taskScriptId()
        ));
    }

    private PreSignedURLIdentifier generateTts(TaskScriptRecord taskScriptRecord) {
        // URL 서명
        var preSignedURLIdentifier = preSignedFileUploadService.sign(contentsLocator, FileType.WAV);

        // tts 파일 생성
        var ttsCreateCommand = new TtsCreateCommand(
                taskScriptRecord.sentence(),
                serverURL,
                preSignedURLIdentifier.fullPath(),
                preSignedURLIdentifier.fileName(),
                preSignedURLIdentifier.uploadId()
        );
        ttsService.create(ttsCreateCommand);

        return preSignedURLIdentifier;
    }

}
