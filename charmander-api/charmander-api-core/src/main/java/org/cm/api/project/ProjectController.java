package org.cm.api.project;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.project.dto.*;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectSummaryService projectSummaryService;

    @MemberOnly
    @GetMapping
    public ListResponse<ProjectResponse> getAllProjects(@AuthUser AuthInfo authInfo) {
        var items = projectService.getAllProjects(authInfo);
        return ListResponse.of(items, ProjectResponse::from);
    }

    @MemberOnly
    @GetMapping("/{id}")
    public ProjectDetailResponse getProjectById(@PathVariable Long id, @AuthUser AuthInfo authInfo) {
        var item = projectService.getProjectByIdForDetails(authInfo, id);
        return ProjectDetailResponse.from(item);
    }

    @MemberOnly
    @PostMapping
    public ProjectDetailResponse createProject(@AuthUser AuthInfo authInfo) {
        var item = projectService.createProject(authInfo);
        return ProjectDetailResponse.from(item);
    }

    @MemberOnly
    @PatchMapping("/{id}/newsArticle")
    public void updateProjectNewsArticle(
        @PathVariable Long id,
        @AuthUser AuthInfo authInfo,
        @RequestBody ProjectUpdateNewsArticleRequest request
    ) {
        projectService.modifyProjectNewsArticle(
            id,
            authInfo.getMemberId(),
            request.newsArticle()
        );
    }

    @MemberOnly
    @PatchMapping("/{id}")
    public void updateProject(@PathVariable Long id, @RequestBody ProjectUpdateRequest request, @AuthUser AuthInfo authInfo) {
        projectService.updateProject(authInfo, id, request);
    }

    @MemberOnly
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id, @AuthUser AuthInfo authInfo) {
        projectService.deleteProject(authInfo, id);
    }

    @MemberOnly
    @PostMapping("/generate-transcript")
    public ProjectGenerateTranscriptAIResult generateTranscript(@RequestBody ProjectGenerateTranscriptRequest request, @AuthUser AuthInfo authInfo) {
        return projectSummaryService.generateTranscript(authInfo, request);
    }

    // TODO: 중복 요청 방지
    @MemberOnly
    @PostMapping("/{id}/generate-video")
    public ProjectGenerationResponse generateProject(@PathVariable Long id, @AuthUser AuthInfo authInfo) {
        var task = projectService.generateProject(authInfo, id);
        return new ProjectGenerationResponse(id, task.getId());
    }
}
