package org.cm.kafka;

public record VideoOverlayRecord(
        Long taskId,
        Long taskScriptId,
        Long sceneId,
        String videoId
) {
}
