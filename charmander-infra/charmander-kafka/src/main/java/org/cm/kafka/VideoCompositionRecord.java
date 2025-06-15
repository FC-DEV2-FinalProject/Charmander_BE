package org.cm.kafka;

public record VideoCompositionRecord(
        Long taskId,
        Long sceneId,
        String sceneVideoId
) {
}
