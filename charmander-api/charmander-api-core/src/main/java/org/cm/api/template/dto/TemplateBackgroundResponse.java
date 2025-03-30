package org.cm.api.template.dto;

import org.cm.domain.template.TemplateBackground;
import org.cm.domain.template.TemplateBackgroundType;
import org.mapstruct.Mapping;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.cm.api.template.dto.TemplateBackgroundResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record TemplateBackgroundResponse(
    Long id,
    Long ownerId,
    String name,
    int priority,
    TemplateBackgroundType type,
    String fileUrl,
    ScreenSizeDto size,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        @Mapping(source = "owner.id", target = "ownerId")
        TemplateBackgroundResponse map(TemplateBackground e);
    }

    public static TemplateBackgroundResponse from(TemplateBackground e) {
        return INSTANCE.map(e);
    }

    public record ScreenSizeDto(
        int width,
        int height
    ) implements Serializable {
    }
}
