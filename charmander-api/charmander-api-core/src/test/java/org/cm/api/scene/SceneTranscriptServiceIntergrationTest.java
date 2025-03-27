package org.cm.api.scene;

import org.cm.api.scene.dto.SceneTranscriptCreateCommand;
import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceIntergrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import({
    SceneTranscriptService.class,
})
class SceneTranscriptServiceIntergrationTest extends BaseServiceIntergrationTest {
    @Autowired
    SceneTranscriptService sceneTranscriptService;

    @Nested
    @DisplayName("[삽입 테스트]")
    class InsertionTest {
        @Test
        @DisplayName("001. 대본을 생성할 수 있어야 함.")
        void 대본을_생성할_수_있어야_함() {
            // stub
            var member = createMember();
            var project = createProject(member);
            var scene = createScene(project);
            var authInfo = new AuthInfo(member.getId());

            // given
            var command = new SceneTranscriptCreateCommand(project.getId(), scene.getId());

            // when
            sceneTranscriptService.createSceneTranscript(authInfo, command);
            var transcripts = scene.getTranscripts();

            // then
            assertEquals(1, transcripts.size());
        }
    }
}
