package org.cm.api;

import java.time.Duration;
import org.cm.domain.task.TaskOutput;
import org.cm.infra.storage.PreSignedURLCompleteCommand;

public record TaskCompleteRequest(
        String fileId,
        int seconds,

        PreSignedURLCompleteCommand preSignedURLCompleteCommand
){

    public TaskOutput toTaskOutput(){
        return new TaskOutput(fileId, Duration.ofSeconds(seconds));
    }
}
