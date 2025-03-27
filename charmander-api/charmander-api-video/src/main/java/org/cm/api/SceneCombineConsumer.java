package org.cm.api;

import lombok.RequiredArgsConstructor;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.task.SceneOutputRepository;
import org.cm.infra.mediaconvert.queue.VideoOverlayCombineQueue;
import org.cm.infra.mediaconvert.queue.WavVideoCombineQueue.VideoSource;
import org.cm.kafka.SceneCombineRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SceneCombineConsumer {

    private final SceneOutputRepository sceneOutputRepository;
    private final VideoOverlayCombineQueue videoOverlayCombineQueue;

    @KafkaListener(topics = "scene-task", groupId = "tts-task-group")
    public void consume(SceneCombineRecord sceneCombineRecord) {
        var sceneOutput = sceneOutputRepository.getById(sceneCombineRecord.sceneId());
        var scene = sceneOutput.getScene();
        var background = scene.background();
        var avatar = scene.avatar();

        videoOverlayCombineQueue.offer(
                new VideoSource(
                        sceneCombineRecord.videoId(),
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
                RandomKeyGenerator.generateRandomKey()
        );


    }


}
