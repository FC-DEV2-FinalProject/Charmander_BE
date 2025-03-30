package org.cm.api.scene.dto;

import lombok.RequiredArgsConstructor;
import org.cm.domain.common.ScreenSize;
import org.cm.domain.file.UploadedFileRepository;
import org.cm.domain.file.UploadedFileStatus;
import org.cm.domain.scene.Scene;
import org.cm.domain.template.TemplateAvatarRepository;
import org.cm.domain.template.TemplateBackgroundRepository;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public record SceneUpdateRequest(
    SceneSubtitleDto subtitle,
    SceneBackgroundUpdateDto background,
    SceneAvatarUpdateDto avatar,
    ScenePropertyDto property
) {
    @Component
    @RequiredArgsConstructor
    public static class Mapper {
        private final TemplateBackgroundRepository templateBackgroundRepository;
        private final TemplateAvatarRepository templateAvatarRepository;
        private final UploadedFileRepository uploadedFileRepository;

        public void update(AuthInfo authInfo, Scene scene, SceneUpdateRequest request) {
            if (request == null) {
                return;
            }

            updateSubtitle(scene, request.subtitle);
            updateBackground(authInfo, scene, request.background);
            updateAvatar(authInfo, scene, request.avatar);
            updateProperty(scene, request.property);
        }

        private void updateSubtitle(Scene scene, SceneSubtitleDto subtitle) {
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

        private void updateBackground(AuthInfo authInfo, Scene scene, SceneBackgroundUpdateDto background) {
            if (background == null) {
                return;
            }

            if (background.templateId() != null) {
                var template = templateBackgroundRepository.findById(background.templateId())
                    .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.TEMPLATE_BACKGROUND_NOT_FOUND));
                scene.setBackground(template.toSceneMedia());
            }
            if (background.fileId() != null) {
                var file = uploadedFileRepository.findByFullPathAndOwnerIdAndStatus(background.fileId(), authInfo.getMemberId(), UploadedFileStatus.COMPLETED)
                    .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.FILE_NOT_FOUND));
                scene.setBackground(file.toSceneMedia());
            }
        }

        private void updateAvatar(AuthInfo authInfo, Scene scene, SceneAvatarUpdateDto avatar) {
            if (avatar == null) {
                return;
            }

            if (avatar.templateId() != null) {
                var template = templateAvatarRepository.findById(avatar.templateId())
                    .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.TEMPLATE_AVATAR_NOT_FOUND));
                scene.setAvatar(template.toSceneMedia());
            }
        }

        private void updateProperty(Scene scene, ScenePropertyDto property) {
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

    public record SceneBackgroundUpdateDto(
        Long templateId,
        String fileId
    ) implements Serializable {
    }

    public record SceneAvatarUpdateDto(
        Long templateId
    ) implements Serializable {
    }

    public record ScenePropertyDto(
        ScreenSize size,
        String background
    ) implements Serializable {
    }
}
