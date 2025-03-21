package org.cm.vo;

public record TaskScriptGenerationCompletedEvent(
        Long taskId,
        Long taskScriptId,
        String fileId,
        int playTime
) {
}
