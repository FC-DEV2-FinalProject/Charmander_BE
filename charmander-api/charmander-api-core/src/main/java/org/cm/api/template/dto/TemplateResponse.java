package org.cm.api.template.dto;

import org.cm.domain.common.ScreenSize;
import org.cm.domain.template.*;

import java.time.LocalDateTime;

public record TemplateResponse(
    Long id,
    int priority,
    TemplateDataDto data,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TemplateResponse from(Template e) {
        return new TemplateResponse(
            e.getId(),
            e.getPriority(),
            TemplateDataDto.from(e.getData()),
            e.getCreatedAt(),
            e.getUpdatedAt()
        );
    }

    public record TemplateDataDto(
        String name,
        Integer categoryId,
        ScreenSize size,
        TemplateBackgroundDto background,
        TemplateAvatarDto avatar
    ) {
        public static TemplateDataDto from(TemplateData e) {
            return new TemplateDataDto(
                e.name(),
                e.category().getValue(),
                e.size(),
                TemplateBackgroundDto.from(e.background()),
                TemplateAvatarDto.from(e.avatar())
            );
        }
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
        public static TemplateBackgroundDto from(TemplateBackground e) {
            return new TemplateBackgroundDto(
                e.getId(),
                e.getName(),
                e.getPriority(),
                e.getType(),
                e.getFileUrl(),
                ScreenSizeDto.from(e.getSize()),
                e.getCreatedAt(),
                e.getUpdatedAt()
            );
        }
    }

    public record TemplateAvatarDto(
        Long id,
        String name,
        int priority,
        String fileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        public static TemplateAvatarDto from(TemplateAvatar e) {
            return new TemplateAvatarDto(
                e.getId(),
                e.getName(),
                e.getPriority(),
                e.getFileUrl(),
                e.getCreatedAt(),
                e.getUpdatedAt()
            );
        }
    }

    public record ScreenSizeDto(
        int width,
        int height
    ) {
        public static ScreenSizeDto from(ScreenSize e) {
            return new ScreenSizeDto(
                e.width(),
                e.height()
            );
        }
    }
}
