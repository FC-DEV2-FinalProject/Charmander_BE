package org.cm.api.scene;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.scene.dto.SceneResponse;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projects/{projectId}")
@RequiredArgsConstructor
public class SceneController {
    private final SceneService sceneService;

    @MemberOnly
    @GetMapping("/scenes")
    public ListResponse<SceneResponse> getProjectScenes(@PathVariable Long projectId, @AuthUser AuthInfo authInfo) {
        var items = sceneService.getProjectScenes(authInfo, projectId);
        return ListResponse.of(items, SceneResponse::from);
    }
}
