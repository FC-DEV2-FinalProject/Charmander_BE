package org.cm.api.scene.dto;

import org.cm.domain.common.ScreenSize;
import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneMedia;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.io.Serializable;

import static org.mapstruct.factory.Mappers.getMapper;

public record SceneUpdateRequest(
    SceneSubtitleDto subtitle,
    SceneMedia background,
    SceneMedia avatar,
    ScenePropertyDto property
) {
    @org.mapstruct.Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        void update(@MappingTarget Scene e, SceneUpdateRequest req);
    }

    public static void update(Scene scene, SceneUpdateRequest request) {
        Mapper.INSTANCE.update(scene, request);
    }

    public record SceneSubtitleDto(
        String text,
        PropertyDto property
    ) implements Serializable {
        public record PropertyDto(
            String fontFamilly,
            String fontSize,
            String fontColor
        ) implements Serializable {
        }
    }

    public record ScenePropertyDto(
        ScreenSize size,
        String background
    ) implements Serializable {
    }
}
