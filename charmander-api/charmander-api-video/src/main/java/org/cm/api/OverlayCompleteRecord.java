package org.cm.api;

public record OverlayCompleteRecord(
        Long taskId,
        Long sceneId,
        String sceneVideoId
) {
}
