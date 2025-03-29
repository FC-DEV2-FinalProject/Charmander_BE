package org.cm.domain.task;

import java.util.List;
import java.util.Objects;
import org.cm.common.domain.SceneMediaType;
import org.cm.domain.common.Position2D;
import org.cm.domain.project.Project;
import org.cm.domain.scene.SceneMedia;
import org.cm.domain.scene.SceneSubtitle;
import org.cm.domain.scene.SceneTranscript;

public record TaskInputSchema(
        String schema_version,
        Long id,
        List<Scene> scenes
) {
    public static TaskInputSchema from(Project pj) {
        return new TaskInputSchema(
                "1.0",
                pj.getId(),
                pj.getScenes().stream().map(Scene::from).toList()
        );
    }

    public Scene findSceneById(Long sceneId) {
        return scenes.stream()
                .filter(scene -> Objects.equals(scene.id, sceneId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    // 장면
    public record Scene(
            Long id,
            List<Transcript> transcript,
            Subtitle subtitle,
            Media background,
            Media avatar
    ) {
        public static Scene from(org.cm.domain.scene.Scene scene) {
            return new Scene(
                    scene.getId(),
                    scene.getTranscripts().stream().map(Transcript::from).toList(),
                    Subtitle.from(scene.getSubtitle()),
                    Media.from(scene.getBackground()),
                    Media.from(scene.getAvatar())
            );
        }
    }

    // 대사
    public record Transcript(
            Long id,
            String text,
            double speed,
            int postDelay
    ) {
        public static Transcript from(SceneTranscript sceneTranscript) {
            return new Transcript(
                    sceneTranscript.getId(),
                    sceneTranscript.getText(),
                    sceneTranscript.getProperty().speed(),
                    sceneTranscript.getProperty().postDelay()
            );
        }
    }

    // 자막
    public record Subtitle(
            String text,
            String fontFamily,
            String fontSize,
            String fontColor
    ) {
        public static Subtitle from(SceneSubtitle subtitle) {
            return new Subtitle(
                    subtitle.getText(),
                    subtitle.getProperty().fontFamilly(),
                    subtitle.getProperty().fontSize(),
                    subtitle.getProperty().fontColor()
            );
        }
    }

    // 화면
    public record Media(
            SceneMediaType type,
            int width,
            int height,
            String url,
            Position2D position,
            double scale
    ) {
        public static Media from(SceneMedia sceneMedia) {
            return new Media(
                    sceneMedia.type(),
                    sceneMedia.property().size().width(),
                    sceneMedia.property().size().height(),
                    sceneMedia.fileId(),
                    sceneMedia.property().position(),
                    sceneMedia.property().scale()
            );
        }
    }
}