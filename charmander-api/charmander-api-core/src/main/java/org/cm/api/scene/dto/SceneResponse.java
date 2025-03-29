package org.cm.api.scene.dto;

import org.cm.domain.common.ScreenSize;
import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneMedia;
import org.cm.domain.scene.SceneSubtitle;
import org.mapstruct.Mapping;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static org.cm.api.scene.dto.SceneResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record SceneResponse(
    Long id,
    Long projectId,
    List<SceneTranscriptDto> transcripts,
    SceneSubtitle subtitle,
    SceneMedia background,
    SceneMedia avatar,
    ScenePropertyDto property,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        @Mapping(source = "project.id", target = "projectId")
        SceneResponse map(Scene e);
    }

    public static SceneResponse from(Scene e) {
        return INSTANCE.map(e);
    }

    public record SceneTranscriptDto(
        Long id,
        String text,
        SceneResponse.SceneTranscriptDto.PropertyDto property,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) implements Serializable {
        public record PropertyDto(
            double speed,
            int postDelay
        ) implements Serializable {
        }
    }

    public record ScenePropertyDto(
        ScreenSize size,
        String background
    ) implements Serializable {
    }
}
