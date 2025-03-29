package org.cm.api.task.event;

import lombok.Getter;
import org.cm.domain.task.Task;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskCreationEvent extends ApplicationEvent {
    private final Task task;

    public TaskCreationEvent(Task task) {
        super(task);
        this.task = task;
    }
}
