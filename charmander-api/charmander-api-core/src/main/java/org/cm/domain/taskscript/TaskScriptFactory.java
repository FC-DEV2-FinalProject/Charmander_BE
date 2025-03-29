package org.cm.domain.taskscript;

import lombok.experimental.UtilityClass;
import org.cm.domain.scene.SceneTranscript;
import org.cm.domain.task.Task;

@UtilityClass
public class TaskScriptFactory {
    public static TaskScript create(Task task, SceneTranscript transcript) {
        return new TaskScript(task, transcript.getScene().getId(), transcript.getText(), transcript.getProperty());
    }
}
