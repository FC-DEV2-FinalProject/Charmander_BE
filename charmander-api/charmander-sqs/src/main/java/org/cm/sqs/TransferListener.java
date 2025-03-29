package org.cm.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.cm.kafka.OverlayCompleteRecord;
import org.cm.kafka.SceneCombineRecord;
import org.cm.kafka.TtsCombineRecord;
import org.cm.kafka.VideoConcatRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SqsListener("${sqs.queue}")
    public void messageListener(MediaConvertJobMessage message) {
        var detail = message.detail();
        var metadata = detail.userMetadata();
        var file = detail.outputGroupDetails()
                .getFirst()
                .outputDetails()
                .getFirst()
                .outputFilePaths()
                .getFirst();

        switch (detail.userMetadata().type()) {
            case TTS_COMBINE -> kafkaTemplate.send("tts-combine-task", new TtsCombineRecord(
                    metadata.taskId(),
                    metadata.taskScriptId(),
                    metadata.sceneId(),
                    file
            ));
            case VIDEO_OVERLAY -> kafkaTemplate.send("video-overlay-task", new SceneCombineRecord(
                    metadata.taskId(),
                    metadata.taskScriptId(),
                    metadata.sceneId(),
                    file
            ));
            case SCENE_COMBINE -> kafkaTemplate.send("video-concat-task", new OverlayCompleteRecord(
                    metadata.taskId(),
                    metadata.sceneId(),
                    file
            ));
            case CONCAT_VIDEO -> kafkaTemplate.send("scene-task", new VideoConcatRecord(
                    metadata.taskId(),
                    file
            ));
        }

    }

}
