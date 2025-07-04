package org.cm.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.task.SceneOutputRepository;
import org.cm.infra.mediaconvert.queue.WavCombineQueue.AudioSource;
import org.cm.infra.mediaconvert.queue.WavImageCombineQueue;
import org.cm.infra.mediaconvert.queue.WavImageCombineQueue.ImageSource;
import org.cm.infra.mediaconvert.queue.WavVideoCombineQueue;
import org.cm.infra.mediaconvert.queue.WavVideoCombineQueue.VideoSource;
import org.cm.kafka.KafkaTopicNames;
import org.cm.kafka.SceneProcessingRecord;
import org.cm.kafka.UserMetadata;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SceneCombineConsumer {

    private final SceneOutputRepository sceneOutputRepository;
    private final WavImageCombineQueue wavImageCombineQueue;
    private final WavVideoCombineQueue wavVideoCombineQueue;

    // TODO 트랜잭션 처리 생각하기
    @Transactional
    @KafkaListener(topics = KafkaTopicNames.SCENE_PROCESSING, groupId = "tts-task-group")
    public void consume(SceneProcessingRecord record) {
        log.info("{}", record);
        var scene = sceneOutputRepository.getById(record.sceneId());

        scene.update(record.ttsFileId());

        var background = scene.getScene().background();
        var sceneFileId = RandomKeyGenerator.generateRandomKey();
        var metadata = UserMetadata.sceneCombine(record.taskId(), record.taskScriptId(), record.sceneId());

        switch (background.type()) {
            case Image -> wavImageCombineQueue.offer(
                    new AudioSource(record.ttsFileId(), 0),
                    new ImageSource(background.url(), background.width(), background.height(), 0, 0),
                    sceneFileId,
                    metadata
            );
            case Video -> wavVideoCombineQueue.offer(
                    new AudioSource(record.ttsFileId(), 0),
                    new VideoSource(background.url(), background.width(), background.height(), 0, 0),
                    sceneFileId,
                    metadata
            );
        }
    }
}
