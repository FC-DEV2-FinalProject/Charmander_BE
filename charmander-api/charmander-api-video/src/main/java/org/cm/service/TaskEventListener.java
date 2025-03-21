package org.cm.service;

import lombok.RequiredArgsConstructor;
import org.cm.vo.TaskCompletedCheckEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskEventListener {

    private final TaskService taskService;

    @EventListener
    public void listen(TaskCompletedCheckEvent event) {
        taskService.tryConvert(event.taskId());
    }
}
