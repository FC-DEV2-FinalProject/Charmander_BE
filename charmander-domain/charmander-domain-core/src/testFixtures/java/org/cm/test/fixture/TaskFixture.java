package org.cm.test.fixture;

import org.cm.domain.project.Project;
import org.cm.domain.task.Task;
import org.cm.domain.task.TaskType;

public class TaskFixture {

    public static Task create(Project project) {
        return new Task(project, TaskType.VIDEO);
    }

    public static Task createWithFail(Project project) {
        var task = create(project);

        task.start();
//        task.fail();

        return task;
    }

    public static Task createWithCancel(Project project) {
        var task = create(project);

        task.start();
        task.cancel();

        return task;
    }
}

