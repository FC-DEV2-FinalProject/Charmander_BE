package org.cm.api;

import lombok.RequiredArgsConstructor;
import org.cm.domain.task.TaskRepository;
import org.cm.kafka.CompletionNotificationRecord;
import org.cm.kafka.KafkaTopicNames;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ConvertCompleteConsumer {

    private final TaskRepository taskRepository;

    @Transactional
    @KafkaListener(topics = KafkaTopicNames.COMPLETION_NOTIFICATION, groupId = "tts-task-group")
    public void listen(CompletionNotificationRecord videoConcatRecord) {
        var task = taskRepository.getById(videoConcatRecord.taskId());

        task.succeed(videoConcatRecord.fileId());
    }
}
