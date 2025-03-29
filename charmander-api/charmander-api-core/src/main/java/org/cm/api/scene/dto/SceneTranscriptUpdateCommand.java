package org.cm.api.scene.dto;

import jakarta.validation.constraints.Max;
import org.cm.domain.scene.SceneTranscript;
import org.jspecify.annotations.NonNull;

public record SceneTranscriptUpdateCommand(
    @NonNull
    Long projectId,
    @NonNull
    Long sceneId,
    @NonNull
    Long transcriptId,
    @Max(value = 300, message = "Text is too long")
    String text,
    Double speed,
    Integer postDelay
) {
    public SceneTranscriptUpdateCommand {
        if (speed != null) {
            if (speed < 0.5 || speed > 2.0) {
                throw new IllegalArgumentException("Speed must be between 0.5 and 2.0");
            }
        }

        if (postDelay != null) {
            if (postDelay < 0 || postDelay > 10000) {
                throw new IllegalArgumentException("Post delay must be between 0 and 10000");
            }
        }
    }

    public void update(SceneTranscript transcript) {
        if (text != null) {
            transcript.setText(text);
        }

        updateProperty(transcript);
    }

    private void updateProperty(SceneTranscript transcript) {
        var property = transcript.getProperty();
        if (speed != null) {
            property = property.withSpeed(speed);
        }
        if (postDelay != null) {
            property = property.withPostDelay(postDelay);
        }
        transcript.setProperty(property);
    }
}
