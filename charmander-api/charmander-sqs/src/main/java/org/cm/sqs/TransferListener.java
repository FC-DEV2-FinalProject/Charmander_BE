package org.cm.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.kafka.CompletionNotificationRecord;
import org.cm.kafka.KafkaTopicNames;
import org.cm.kafka.SceneProcessingRecord;
import org.cm.kafka.VideoCompositionRecord;
import org.cm.kafka.VideoOverlayRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SqsListener("${sqs.queue}")
    public void messageListener(MediaConvertJobMessage message) {
        var detail = message.detail();
        var metadata = detail.userMetadata();

        if (Objects.equals(detail.status(), "ERROR")) {
            log.info("error");
            return;
        }
        log.info(">>>>>> {}", message);
        var file = detail.outputGroupDetails()
                .getFirst()
                .outputDetails()
                .getFirst()
                .outputFilePaths()
                .getFirst();

        if (detail.userMetadata().type() == null) {
            return;
        }

        log.info("{} {}", detail.jobId(), detail.userMetadata());

        // tts 결합
        // scene 결합
        // scene - avatar 결합
        // video 결합

        switch (detail.userMetadata().type()) {
            case TTS_COMBINE -> kafkaTemplate.send(
                    KafkaTopicNames.SCENE_PROCESSING,
                    new SceneProcessingRecord(
                            metadata.taskId(),
                            metadata.taskScriptId(),
                            metadata.sceneId(),
                            file
                    )
            );
            case SCENE_COMBINE -> kafkaTemplate.send(
                    KafkaTopicNames.OVERLAY_PROCESSING,
                    new VideoOverlayRecord(
                            metadata.taskId(),
                            metadata.taskScriptId(),
                            metadata.sceneId(),
                            file
                    )
            );
            case VIDEO_OVERLAY -> kafkaTemplate.send(
                    KafkaTopicNames.VIDEO_COMPOSITION,
                    new VideoCompositionRecord(
                            metadata.taskId(),
                            metadata.sceneId(),
                            file
                    )
            );
            case CONCAT_VIDEO -> kafkaTemplate.send(
                    KafkaTopicNames.COMPLETION_NOTIFICATION,
                    new CompletionNotificationRecord(
                            metadata.taskId(),
                            file
                    )
            );
        }
    }
}
