package org.cm.kafka;

public record SceneCombineRecord(
        Long taskId,
        Long sceneId,
        String videoId
) {
}
