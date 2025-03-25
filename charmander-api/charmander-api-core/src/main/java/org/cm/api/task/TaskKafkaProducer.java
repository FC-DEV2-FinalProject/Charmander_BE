package org.cm.api.task;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cm.api.task.event.TaskCreationEvent;
import org.cm.domain.task.TaskRepository;
import org.cm.api.task.dto.TaskScriptRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TaskKafkaProducer implements ApplicationEventPublisherAware {
    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final TaskRepository taskRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION, classes = TaskCreationEvent.class)
    public void handleProjectGeneratedEvent(TaskCreationEvent event) {
        event.getTask()
            .getTaskScripts()
            .stream()
            .map(TaskScriptRecord::from)
            .forEach((message) -> kafkaTemplate.send("tts-task", message));
    }
}
