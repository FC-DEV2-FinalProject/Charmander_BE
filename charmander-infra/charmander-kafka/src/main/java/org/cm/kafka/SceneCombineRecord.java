package org.cm.kafka;

public record SceneCombineRecord(
        Long taskId,
        Long taskScriptId,
        Long sceneId,
        String videoId
) {
}
