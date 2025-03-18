package org.cm.fixture;

import org.cm.domain.project.Project;
import org.cm.domain.task.Task;
import org.cm.domain.task.TaskType;

public class TaskFixture {

    public static Task create(Project project) {
        return new Task(project, TaskType.VIDEO);
    }
}
