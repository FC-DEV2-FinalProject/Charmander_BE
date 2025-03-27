package org.cm.api.scene;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cm.api.scene.dto.SceneUpdateRequest;
import org.cm.domain.project.ProjectRepository;
import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneRepository;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SceneService {
    private final ProjectRepository projectRepository;
    private final SceneRepository sceneRepository;

    private final SceneUpdateRequest.Mapper updateMapper;

    public List<Scene> getProjectScenes(AuthInfo authInfo, Long projectId) {
        return sceneRepository.findProjectScenes(projectId, authInfo.getMemberId());
    }

    public Scene createScene(AuthInfo authInfo, Long projectId) {
        var project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));

        var scene = Scene.createEmpty(project);
        return sceneRepository.save(scene);
    }

    public void deleteScene(AuthInfo authInfo, Long projectId, Long sceneId) {
        var scene = sceneRepository.findProjectSceneForUpdate(projectId, sceneId, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.SCENE_NOT_FOUND));

        sceneRepository.delete(scene);
    }

    public void updateScene(AuthInfo authInfo, Long projectId, Long sceneId, SceneUpdateRequest request) {
        var scene = sceneRepository.findProjectSceneForUpdate(projectId, sceneId, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.SCENE_NOT_FOUND));

        SceneUpdateRequest.update(scene, request);
    }
}
