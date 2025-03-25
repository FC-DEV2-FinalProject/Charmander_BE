package org.cm.service;

import lombok.RequiredArgsConstructor;
import org.cm.vo.TaskScriptGenerationCompletedEvent;
import org.cm.vo.TaskScriptGenerationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskScriptEventListener {

    private final TaskScriptService taskScriptService;

    @EventListener
    public void listen(TaskScriptGenerationStartedEvent event) {
        taskScriptService.start(event.taskScriptId());
    }

    @EventListener
    public void listen(TaskScriptGenerationCompletedEvent event) {
        taskScriptService.complete(event.taskScriptId(), event.fileId());
    }

}
