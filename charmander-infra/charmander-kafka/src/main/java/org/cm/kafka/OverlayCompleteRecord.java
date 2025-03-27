package org.cm.kafka;

public record OverlayCompleteRecord(
        Long taskId,
        Long sceneId,
        String sceneVideoId
) {
}
