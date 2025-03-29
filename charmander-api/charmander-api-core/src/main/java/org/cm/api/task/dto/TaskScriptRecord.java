package org.cm.api.task.dto;

import org.cm.domain.taskscript.TaskScript;

public record TaskScriptRecord(
    Long taskId,
    Long taskScriptId,
    String sentence,
    String option
) {
    public static TaskScriptRecord from(TaskScript taskScript) {
        return new TaskScriptRecord(
            taskScript.getTask().getId(),
            taskScript.getId(),
            taskScript.getSentence(),
            taskScript.getOption()
        );
    }
}
