package org.cm.api.template.dto;

import org.cm.domain.common.ScreenSize;
import org.cm.domain.template.Template;
import org.cm.domain.template.TemplateAvatarType;
import org.cm.domain.template.TemplateBackgroundType;
import org.cm.domain.template.TemplateCategory;

import java.time.LocalDateTime;

import static org.mapstruct.factory.Mappers.getMapper;

public record TemplateResponse(
    Long id,
    int priority,
    String thumbnailUrl,
    TemplateData data,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        TemplateResponse map(Template e);
    }

    public static TemplateResponse from(Template e) {
        return Mapper.INSTANCE.map(e);
    }

    public record TemplateData(
        String name,
        TemplateCategory category,
        ScreenSize size,
        TemplateBackgroundDto background,
        TemplateAvatarDto avatar
    ) {
    }

    public record TemplateBackgroundDto(
        Long id,
        String name,
        int priority,
        TemplateBackgroundType type,
        String fileUrl,
        ScreenSizeDto size,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {

    }

    public record TemplateAvatarDto(
        Long id,
        String name,
        int priority,
        TemplateAvatarType type,
        String fileUrl,
        ScreenSizeDto size,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
    }

    public record ScreenSizeDto(
        int width,
        int height
    ) {
    }
}
