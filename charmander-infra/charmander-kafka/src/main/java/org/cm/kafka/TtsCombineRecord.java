package org.cm.kafka;

public record TtsCombineRecord(
        Long taskId,
        Long sceneId,
        String ttsFileId
) {
}
