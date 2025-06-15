package org.cm.api.template.dto;

import org.cm.domain.template.TemplateAvatar;
import org.cm.domain.template.TemplateAvatarType;
import org.cm.domain.template.TemplateBackgroundType;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.cm.api.template.dto.TemplateAvatarResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record TemplateAvatarResponse(
    Long id,
    Long ownerId,
    String name,
    int priority,
    TemplateAvatarType type,
    String fileUrl,
    ScreenSizeDto size,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        TemplateAvatarResponse map(TemplateAvatar e);
    }

    public static TemplateAvatarResponse from(TemplateAvatar e) {
        return INSTANCE.map(e);
    }

    public record ScreenSizeDto(
        int width,
        int height
    ) implements Serializable {
    }
}
