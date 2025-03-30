package org.cm.api.scene;

import org.cm.api.scene.dto.SceneTranscriptCreateCommand;
import org.cm.api.scene.dto.SceneTranscriptDeleteCommand;
import org.cm.exception.CoreApiException;
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
@DisplayName("[통합 테스트] SceneTranscriptService")
class SceneTranscriptServiceIntergrationTest extends BaseServiceIntergrationTest {
    @Autowired
    SceneTranscriptService sceneTranscriptService;

    @Nested
    @DisplayName("[삽입]")
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

    @Nested
    @DisplayName("[삭제]")
    class DeletionTest {
        @Test
        @DisplayName("001. 소유한 대본을 삭제할 수 있어야 함.")
        void 소유한_대본을_삭제할_수_있어야_함() {
            // stub
            var member = createMember();
            var project = createProject(member);
            var scene = createScene(project);
            var authInfo = new AuthInfo(member.getId());
            var transcript = createSceneTranscript(scene);

            // given
            var command = new SceneTranscriptDeleteCommand(project.getId(), scene.getId(), transcript.getId());

            // when
            sceneTranscriptService.deleteSceneTranscript(authInfo, command);
            var transcripts = scene.getTranscripts();

            // then
            assertEquals(0, transcripts.size());
        }

        @Test
        @DisplayName("002. 소유하지 않은 대본을 삭제할 수 없어야 함.")
        void 소유하지_않은_대본을_삭제할_수_없어야_함() {
            // stub
            var member = createMember();
            var project = createProject(member);
            var scene = createScene(project);
            var authInfo = new AuthInfo(member.getId());
            var transcript = createSceneTranscript(scene);

            // given
            var command = new SceneTranscriptDeleteCommand(project.getId(), scene.getId(), transcript.getId());
            var otherMember = createMember();
            var otherAuthInfo = new AuthInfo(otherMember.getId());

            // when
            assertThrows(CoreApiException.class, () -> sceneTranscriptService.deleteSceneTranscript(otherAuthInfo, command));
        }
    }
}
