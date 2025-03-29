package org.cm.api.scene.dto;

import java.io.Serializable;

public record SceneTranscriptUpdateRequest(
    String text,
    PropertyDto property
) {
    public SceneTranscriptUpdateCommand toCommand(Long projectId, Long sceneId, Long tsId) {
        return new SceneTranscriptUpdateCommand(
            projectId,
            sceneId,
            tsId,
            text,
            property == null ? null : property.speed,
            property == null ? null : property.postDelay
        );
    }

    public record PropertyDto(
        Double speed,
        Integer postDelay
    ) implements Serializable {
    }
}
