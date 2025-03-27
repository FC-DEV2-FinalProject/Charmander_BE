package org.cm.api.scene.dto;

import lombok.RequiredArgsConstructor;
import org.cm.domain.common.ScreenSize;
import org.cm.domain.scene.Scene;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public record SceneUpdateRequest(
    SceneSubtitleDto subtitle,
    ScenePropertyDto property
) {
    public static void update(Scene scene, SceneUpdateRequest request) {
        Mapper.update(scene, request);
    }

    @Component
    @RequiredArgsConstructor
    public static class Mapper {
        public static void update(Scene scene, SceneUpdateRequest request) {
            if (request == null) {
                return;
            }

            updateSubtitle(scene, request.subtitle);
            updateProperty(scene, request.property);
        }

        private static void updateSubtitle(Scene scene, SceneSubtitleDto subtitle) {
            if (subtitle == null) {
                return;
            }

            var t = scene.getSubtitle();
            if (subtitle.text != null) {
                t.setText(subtitle.text);
            }
            if (subtitle.property() != null) {
                var p = t.getProperty();
                if (subtitle.property().fontFamilly() != null) {
                    p = p.withFontColor(subtitle.property().fontFamilly());
                }
                if (subtitle.property().fontSize() != null) {
                    p = p.withFontColor(subtitle.property().fontSize());
                }
                if (subtitle.property().fontColor() != null) {
                    p = p.withFontColor(subtitle.property().fontColor());
                }
                t.setProperty(p);
            }
        }

        private static void updateProperty(Scene scene, ScenePropertyDto property) {
            if (property == null) {
                return;
            }

            var p = scene.getProperty();
            if (property.size() != null) {
                p = p.withSize(property.size());
            }
            if (property.background() != null) {
                p = p.withBackground(property.background());
            }
            scene.setProperty(p);
        }
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
