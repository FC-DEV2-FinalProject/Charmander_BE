package org.cm.kafka;

public record TaskScriptRecord(
        Long taskId,
        Long taskScriptId,
        String sentence,
        String option
) {
}
