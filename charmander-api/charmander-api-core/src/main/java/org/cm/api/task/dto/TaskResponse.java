package org.cm.api.task.dto;

import org.cm.domain.task.Task;
import org.cm.domain.task.TaskOutput;
import org.cm.domain.task.TaskStatus;
import org.cm.domain.task.TaskType;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

import static org.cm.api.task.dto.TaskResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record TaskResponse(
    Long id,
    Long projectId,
    String jobId,
    TaskType type,
    TaskStatus status,
    TaskOutput output,
    int retryCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        @Mapping(source = "project.id", target = "projectId")
        TaskResponse map(Task e);
    }

    public static TaskResponse from(Task e) {
        return INSTANCE.map(e);
    }
}
