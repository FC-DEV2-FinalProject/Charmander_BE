package org.cm.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.task.SceneOutputRepository;
import org.cm.infra.mediaconvert.queue.AllVideoCombineQueue;
import org.cm.infra.mediaconvert.queue.WavVideoCombineQueue.VideoSource;
import org.cm.kafka.KafkaTopicNames;
import org.cm.kafka.UserMetadata;
import org.cm.kafka.VideoCompositionRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VideoConcatConsumer {

    private final SceneOutputRepository sceneOutputRepository;
    private final AllVideoCombineQueue allVideoCombineQueue;

    @Transactional
    @KafkaListener(topics = KafkaTopicNames.VIDEO_COMPOSITION, groupId = "tts-task-group")
    public void listen(VideoCompositionRecord record) {
        sceneOutputRepository.getById(record.sceneId()).updateSceneId(record.sceneVideoId());

        var videoSources = getVideoSources(record);

        var metadata = UserMetadata.concatVideo(record.taskId(), record.sceneId());

        allVideoCombineQueue.offer(
                videoSources,
                RandomKeyGenerator.generateRandomKey(),
                metadata
        );
    }

    private List<VideoSource> getVideoSources(VideoCompositionRecord record) {
        return sceneOutputRepository.findAllByTaskId(record.taskId())
                .stream()
                .map(sceneOutput -> new VideoSource(sceneOutput.getSceneFileId(), 1280, 720, 0, 0))
                .toList();
    }
}
