package org.cm.api.task.dto;

import org.cm.domain.task.TaskOutput;
import org.cm.domain.task.TaskStatus;
import org.cm.domain.task.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public record TaskResponse(
    Long id,
    Long projectId,
    String jobId,
    TaskType type,
    TaskStatus status,
    TaskOutputDTO output,
    int retryCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record TaskOutputDTO(
        String fileUrl,
        String fileName,
        Duration playtime,
        Integer downloadCount
    ) {
        public TaskOutputDTO(TaskOutput taskOutput) {
            this(
                taskOutput.fileUrl(),
                taskOutput.fileName(),
                taskOutput.playtime(),
                taskOutput.getDownloadCount()
            );
        }
    }
}
