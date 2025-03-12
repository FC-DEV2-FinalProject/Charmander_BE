package org.cm.api.task;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.task.dto.TaskResponse;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public TaskResponse getTask(@AuthUser AuthInfo authInfo, Long taskId) {
        var item = taskService.getMemberTask(taskId, authInfo);
        return TaskResponse.from(item);
    }

    @MemberOnly
    @PostMapping("/{taskId}/retry")
    public TaskResponse retryTask(@AuthUser AuthInfo authInfo, Long taskId) {
        var item = taskService.retryTask(taskId, authInfo);
        return TaskResponse.from(item);
    }
}
