package org.cm.api.task;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.task.dto.TaskResponse;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @MemberOnly
    @GetMapping
    public ListResponse<TaskResponse> getTasks(@AuthUser AuthInfo authInfo) {
        var items = taskService.getMemberTasks(authInfo);
        return ListResponse.of(items, TaskResponse::from);
    }

    @MemberOnly
    @GetMapping("/{taskId}")
    public TaskResponse getTask(@PathVariable Long taskId, @AuthUser AuthInfo authInfo) {
        var item = taskService.getMemberTask(taskId, authInfo);
        return TaskResponse.from(item);
    }

    @MemberOnly
    @PostMapping("/{taskId}/retry")
    public TaskResponse retryTask(@PathVariable Long taskId, @AuthUser AuthInfo authInfo) {
        var item = taskService.retryTask(taskId, authInfo);
        return TaskResponse.from(item);
    }
}
