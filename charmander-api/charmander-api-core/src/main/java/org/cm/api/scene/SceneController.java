package org.cm.api.scene;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.scene.dto.SceneResponse;
import org.cm.api.scene.dto.SceneTranscriptUpdateRequest;
import org.cm.api.scene.dto.SceneUpdateRequest;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}")
@RequiredArgsConstructor
public class SceneController {
    private final SceneService sceneService;
    private final SceneTranscriptService sceneTranscriptService;

    @MemberOnly
    @GetMapping("/scenes")
    public ListResponse<SceneResponse> getProjectScenes(@PathVariable Long projectId, @AuthUser AuthInfo authInfo) {
        var items = sceneService.getProjectScenes(authInfo, projectId);
        return ListResponse.of(items, SceneResponse::from);
    }

    @MemberOnly
    @PostMapping("/scenes")
    public SceneResponse createScene(@PathVariable Long projectId, @AuthUser AuthInfo authInfo) {
        var scene = sceneService.createScene(authInfo, projectId);
        return SceneResponse.from(scene);
    }

    @MemberOnly
    @PatchMapping("/scenes/{sceneId}")
    public void updateScene(@PathVariable Long projectId, @PathVariable Long sceneId, @RequestBody SceneUpdateRequest request, @AuthUser AuthInfo authInfo) {
        sceneService.updateScene(authInfo, projectId, sceneId, request);
    }

    @MemberOnly
    @PatchMapping("/scenes/{sceneId}/ts/{tsId}")
    public void updateSceneTs(
        @PathVariable Long projectId,
        @PathVariable Long sceneId,
        @PathVariable Long tsId,
        @RequestBody SceneTranscriptUpdateRequest request,
        @AuthUser AuthInfo authInfo
    ) {
        var command = request.toCommand(projectId, sceneId, tsId);
        sceneTranscriptService.updateSceneTranscript(authInfo, command);
    }

    @MemberOnly
    @DeleteMapping("/scenes/{sceneId}")
    public void deleteScene(@PathVariable Long projectId, @PathVariable Long sceneId, @AuthUser AuthInfo authInfo) {
        sceneService.deleteScene(authInfo, projectId, sceneId);
    }
}
