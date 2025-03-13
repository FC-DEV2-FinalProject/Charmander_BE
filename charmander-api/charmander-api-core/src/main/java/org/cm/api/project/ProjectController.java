package org.cm.api.project;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.project.dto.ProjectDetailResponse;
import org.cm.api.project.dto.ProjectResponse;
import org.cm.api.project.dto.ProjectUpdateRequest;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @MemberOnly
    @GetMapping
    public ListResponse<ProjectResponse> getAllProjects(@AuthUser AuthInfo authInfo) {
        var items = projectService.getAllProjects(authInfo);
        return ListResponse.of(items, ProjectResponse::from);
    }

    @MemberOnly
    @GetMapping("/{id}")
    public ProjectDetailResponse getProjectById(@PathVariable Long id, @AuthUser AuthInfo authInfo) {
        var item = projectService.getProjectById(authInfo, id);
        return ProjectDetailResponse.from(item);
    }

    @MemberOnly
    @PostMapping
    public ProjectDetailResponse createProject(@AuthUser AuthInfo authInfo) {
        var item = projectService.createProject(authInfo);
        return ProjectDetailResponse.from(item);
    }

    @MemberOnly
    @PutMapping("/{id}")
    public ProjectDetailResponse updateProject(@PathVariable Long id, @RequestBody ProjectUpdateRequest project, @AuthUser AuthInfo authInfo) {
        var item = projectService.updateProject(authInfo, id, project);
        return ProjectDetailResponse.from(item);
    }

    @MemberOnly
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id, @AuthUser AuthInfo authInfo) {
        projectService.deleteProject(authInfo, id);
    }
}
