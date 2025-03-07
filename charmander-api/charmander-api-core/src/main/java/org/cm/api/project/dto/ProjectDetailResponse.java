package org.cm.api.project.dto;

import java.time.LocalDateTime;
import org.cm.domain.project.Project;
import org.cm.domain.project.ProjectStatus;
import org.mapstruct.factory.Mappers;

public record ProjectDetailResponse(
    Long id,
    String name,
    boolean active,
    ProjectStatus status,
    String data,
    int version,
    LocalDateTime lastAccessedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = Mappers.getMapper(Mapper.class);

        ProjectDetailResponse map(Project project);
    }

    public static ProjectDetailResponse of(Project project) {
        return Mapper.INSTANCE.map(project);
    }
}
