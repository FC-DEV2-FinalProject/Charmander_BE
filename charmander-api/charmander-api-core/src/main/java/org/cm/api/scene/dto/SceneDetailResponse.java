package org.cm.api.scene.dto;

import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneMedia;
import org.cm.domain.scene.SceneProperty;
import org.cm.domain.scene.SceneSubtitle;
import org.mapstruct.Mapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static org.cm.api.scene.dto.SceneDetailResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

/**
 * DTO for {@link org.cm.domain.scene.Scene}
 */

public record SceneDetailResponse(
    Long id,
    SceneSubtitle subtitle,
    SceneMedia background,
    SceneMedia avatar,
    SceneProperty property,
    List<SceneTranscriptDto> transcripts,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) implements Serializable {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        SceneDetailResponse map(Scene e);
    }

    public static SceneDetailResponse from(Scene e) {
        return INSTANCE.map(e);
    }
}
