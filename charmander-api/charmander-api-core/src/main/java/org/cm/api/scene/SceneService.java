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

    // TODO: 동시성 이슈
    public void deleteScene(AuthInfo authInfo, Long projectId, Long sceneId) {
        var scene = sceneRepository.findProjectSceneForUpdate(projectId, sceneId, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.SCENE_NOT_FOUND));
        if (sceneRepository.countByProjectId(scene.getProject().getId()) <= 1) {
            throw new CoreApiException(CoreApiExceptionCode.SCENE_DELETE_LAST_SCENE);
        }
        scene.getProject().removeScene(scene);
        sceneRepository.delete(scene);
    }

    public Scene updateScene(AuthInfo authInfo, Long projectId, Long sceneId, SceneUpdateRequest request) {
        var scene = sceneRepository.findProjectSceneForUpdate(projectId, sceneId, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.SCENE_NOT_FOUND));

        updateMapper.update(authInfo, scene, request);
        return sceneRepository.save(scene);
    }
}
