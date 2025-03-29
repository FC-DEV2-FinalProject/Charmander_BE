package org.cm.api.task;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cm.api.task.event.TaskCreationEvent;
import org.cm.kafka.KafkaTopicNames;
import org.cm.kafka.TaskScriptRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TaskKafkaProducer implements ApplicationEventPublisherAware {
    private final KafkaTemplate<String, TaskScriptRecord> kafkaTemplate;
    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION, classes = TaskCreationEvent.class)
    public void handleProjectGeneratedEvent(TaskCreationEvent event) {
        event.getTask()
                .getTaskScripts()
                .stream()
                .map(taskScript -> new TaskScriptRecord(
                        taskScript.getTask().getId(),
                        taskScript.getId(),
                        taskScript.getSentence(),
                        taskScript.getOption()
                ))
                .forEach((message) -> kafkaTemplate.send(KafkaTopicNames.TTS_PROCESSING, message));
    }
}
