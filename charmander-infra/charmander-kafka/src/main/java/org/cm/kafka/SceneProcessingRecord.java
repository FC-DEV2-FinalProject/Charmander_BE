package org.cm.kafka;

public record SceneProcessingRecord(
        Long taskId,
        Long taskScriptId,
        Long sceneId,
        String ttsFileId
) {
}
