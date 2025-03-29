package org.cm.api;

import lombok.RequiredArgsConstructor;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.task.SceneOutputRepository;
import org.cm.infra.mediaconvert.queue.VideoOverlayCombineQueue;
import org.cm.infra.mediaconvert.queue.WavVideoCombineQueue.VideoSource;
import org.cm.kafka.SceneCombineRecord;
import org.cm.kafka.UserMetadata;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SceneCombineConsumer {

    private final SceneOutputRepository sceneOutputRepository;
    private final VideoOverlayCombineQueue videoOverlayCombineQueue;

    @Transactional
    @KafkaListener(topics = "video-overlay-task", groupId = "tts-task-group")
    public void consume(SceneCombineRecord record) {
        var sceneOutput = sceneOutputRepository.getById(record.sceneId());

        sceneOutput.updateSceneId(record.videoId());

        var scene = sceneOutput.getScene();
        var background = scene.background();
        var avatar = scene.avatar();
        var metadata = UserMetadata.videoOverlay(record.taskId(), record.taskScriptId(), record.sceneId());

        videoOverlayCombineQueue.offer(
                new VideoSource(
                        record.videoId(),
                        background.width(),
                        background.height(),
                        0,
                        0
                ),
                new VideoSource(
                        avatar.url(),
                        1280,
                        720,
                        avatar.position().x(),
                        avatar.position().y()
                ),
                RandomKeyGenerator.generateRandomKey(),
                metadata
        );


    }


}
