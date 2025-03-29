package org.cm.api.project.dto;

import org.cm.api.scene.dto.SceneDetailResponse;
import org.cm.domain.project.Project;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponse(
    Long id,
    Long ownerId,
    String name,
    String newsArticle,
    ProjectPropertyDto property,
    List<SceneDetailResponse> scenes,
    String data,
    int version,
    LocalDateTime lastAccessedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = Mappers.getMapper(Mapper.class);

        @Mapping(source = "owner.id", target = "ownerId")
        ProjectDetailResponse map(Project project);
    }

    public static ProjectDetailResponse from(Project project) {
        return Mapper.INSTANCE.map(project);
    }

    public record ProjectPropertyDto(
        ScreenSizeDto screenSize
    ) implements Serializable {
        public record ScreenSizeDto(
            int width,
            int height
        ) implements Serializable {
        }
    }
}
