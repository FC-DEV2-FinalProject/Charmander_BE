package org.cm.api.scene.dto;

import org.cm.domain.scene.SceneTranscript;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.cm.api.scene.dto.SceneTranscriptDto.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;


public record SceneTranscriptDto(
    Long id,
    String text,
    PropertyDto property,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) implements Serializable {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        SceneTranscriptDto map(SceneTranscript e);
    }

    public static SceneTranscriptDto from(SceneTranscript e) {
        return INSTANCE.map(e);
    }

    public record PropertyDto(
        Double speed,
        Integer postDelay
    ) implements Serializable {
    }
}
