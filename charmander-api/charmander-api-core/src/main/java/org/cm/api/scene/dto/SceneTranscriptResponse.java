package org.cm.api.scene.dto;

import org.cm.domain.scene.SceneTranscript;
import org.mapstruct.Mapping;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.cm.api.scene.dto.SceneTranscriptResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record SceneTranscriptResponse(
    Long id,
    Long sceneId,
    String text,
    PropertyDto property,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        @Mapping(source = "scene.id", target = "sceneId")
        SceneTranscriptResponse map(SceneTranscript e);
    }

    public static SceneTranscriptResponse from(SceneTranscript e) {
        return INSTANCE.map(e);
    }

    public record PropertyDto(
        Double speed,
        Integer postDelay
    ) implements Serializable {
    }
}
