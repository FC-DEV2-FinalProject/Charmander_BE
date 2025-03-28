package org.cm.api;

import lombok.RequiredArgsConstructor;
import org.cm.domain.task.TaskRepository;
import org.cm.kafka.VideoConcatRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ConvertCompleteConsumer {

    private final TaskRepository taskRepository;

    @Transactional
    @KafkaListener(topics = "scene-task", groupId = "tts-task-group")
    public void listen(VideoConcatRecord videoConcatRecord) {
        var task = taskRepository.getById(videoConcatRecord.taskId());

        task.succeed(videoConcatRecord.fileId());
    }
}
