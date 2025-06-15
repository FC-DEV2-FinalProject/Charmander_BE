package org.cm.api.scene.dto;

public record SceneTranscriptDeleteCommand(
    Long projectId,
    Long sceneId,
    Long transcriptId
) {
}
