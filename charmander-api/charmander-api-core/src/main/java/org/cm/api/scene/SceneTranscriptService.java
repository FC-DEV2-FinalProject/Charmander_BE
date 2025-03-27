package org.cm.api.scene;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cm.api.scene.dto.SceneTranscriptUpdateCommand;
import org.cm.domain.scene.SceneTranscript;
import org.cm.domain.scene.SceneTranscriptRepository;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SceneTranscriptService {
    private final SceneTranscriptRepository sceneTranscriptRepository;

    public SceneTranscript updateSceneTranscript(
        AuthInfo authInfo,
        @Valid SceneTranscriptUpdateCommand command
    ) {
        var transcript = sceneTranscriptRepository.findForUpdate(command.projectId(), command.transcriptId(), command.sceneId(), authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.SCENE_TRANSCRIPT_NOT_FOUND));

        command.update(transcript);

        return sceneTranscriptRepository.save(transcript);
    }
}
