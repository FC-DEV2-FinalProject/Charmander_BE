package org.cm.api.project.dto;

import java.time.LocalDateTime;
import org.cm.domain.project.Project;
import org.mapstruct.factory.Mappers;

public record ProjectResponse(
    Long id,
    String name,
    boolean active,
    int version,
    LocalDateTime lastAccessedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = Mappers.getMapper(Mapper.class);

        ProjectResponse map(Project project);
    }

    public static ProjectResponse from(Project project) {
        return Mapper.INSTANCE.map(project);
    }
}
