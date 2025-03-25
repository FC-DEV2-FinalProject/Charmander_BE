package org.cm.api;

public record TaskScriptRecord(
        Long taskId,
        Long taskScriptId,
        String sentence,
        String option
) {
}
