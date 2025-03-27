package org.cm.kafka;

public record TtsCombineRecord(
        Long taskId,
        Long taskScriptId,
        Long sceneId,
        String ttsFileId
) {
}
