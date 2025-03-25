package org.cm.vo;

public record TaskScriptGenerationStartedEvent(
        Long taskId,
        Long taskScriptId
) {
}
