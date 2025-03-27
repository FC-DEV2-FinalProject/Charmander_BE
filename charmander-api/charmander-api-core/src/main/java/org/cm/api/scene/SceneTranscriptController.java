package org.cm.api.scene;

import lombok.RequiredArgsConstructor;
import org.cm.api.scene.dto.SceneTranscriptCreateCommand;
import org.cm.api.scene.dto.SceneTranscriptResponse;
import org.cm.api.scene.dto.SceneTranscriptUpdateRequest;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}")
@RequiredArgsConstructor
public class SceneTranscriptController {
    private final SceneTranscriptService sceneTranscriptService;

    @MemberOnly
    @PostMapping("/scenes/{sceneId}/ts")
    public SceneTranscriptResponse createSceneTs(
        @PathVariable Long projectId,
        @PathVariable Long sceneId,
        @AuthUser AuthInfo authInfo
    ) {
        var command = new SceneTranscriptCreateCommand(projectId, sceneId);
        var transcript = sceneTranscriptService.createSceneTranscript(authInfo, command);
        return SceneTranscriptResponse.from(transcript);
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
}
