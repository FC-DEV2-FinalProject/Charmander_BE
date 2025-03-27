package org.cm.api.scene.dto;

import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneTranscript;
import org.jspecify.annotations.NonNull;

public record SceneTranscriptCreateCommand(
    @NonNull
    Long projectId,

    @NonNull
    Long sceneId
) {
    public SceneTranscript toEntity(Scene scene) {
        var transcript = new SceneTranscript(
            scene,
            "",
            SceneTranscript.Property.createDefault()
        );
        scene.addTranscript(transcript);
        return transcript;
    }
}
