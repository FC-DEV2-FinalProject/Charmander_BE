package org.cm.api.scene;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneRepository;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SceneService {
    private final SceneRepository sceneRepository;

    public List<Scene> getProjectScenes(AuthInfo authInfo, Long projectId) {
        return sceneRepository.findProjectScenes(projectId, authInfo.getMemberId());
    }
}
