package org.cm.api.project.dto;

import java.time.LocalDateTime;
import org.cm.domain.project.Project;
import org.mapstruct.factory.Mappers;

public record ProjectDetailResponse(
    Long id,
    String name,
    boolean active,
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

    public static ProjectDetailResponse from(Project project) {
        return Mapper.INSTANCE.map(project);
    }
}
