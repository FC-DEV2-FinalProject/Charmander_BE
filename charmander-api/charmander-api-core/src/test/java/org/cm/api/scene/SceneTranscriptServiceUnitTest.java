package org.cm.api.scene;

import org.cm.api.scene.dto.SceneTranscriptUpdateCommand;
import org.cm.domain.scene.SceneTranscriptRepository;
import org.cm.exception.CoreApiException;
import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceUnitTest;
import org.cm.test.fixture.MemberFixture;
import org.cm.test.fixture.SceneTranscriptFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SceneTranscriptServiceUnitTest extends BaseServiceUnitTest {
    @InjectMocks
    SceneTranscriptService SceneTranscriptService;

    @Mock
    SceneTranscriptRepository sceneTranscriptRepository;

    @Nested
    @DisplayName("[수정]")
    class UpdateTest {
        @Test
        @DisplayName("001. 대본 문장을 수정할 수 있다.")
        void 대본_문장을_수정할_수_있다() {
            // stub
            var member = MemberFixture.create();
            var ts = SceneTranscriptFixture.create();
            var authInfo = new AuthInfo(member.getId());
            Mockito
                .when(sceneTranscriptRepository.findForUpdate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(ts));

            // when
            var text = "이것은 변경된 텍스트 입니다.";
            var command = new SceneTranscriptUpdateCommand(1L, 1L, 1L, text, ts.getProperty().speed(), ts.getProperty().postDelay());

            // when
            var updatedTs = SceneTranscriptService.updateSceneTranscript(authInfo, command);

            // then
            assertEquals(text, updatedTs.getText());
        }

        @Test
        @DisplayName("002. 대본 속도를 수정할 수 있다")
        void 대본_속도를_수정할_수_있다() {
            // stub
            var member = MemberFixture.create();
            var ts = SceneTranscriptFixture.create();
            var authInfo = new AuthInfo(member.getId());
            Mockito
                .when(sceneTranscriptRepository.findForUpdate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(ts));

            // when
            var text = "이것은 변경된 텍스트 입니다.";
            var speed = 2.0;
            var command = new SceneTranscriptUpdateCommand(1L, 1L, 1L, text, speed, ts.getProperty().postDelay());

            // when
            var updatedTs = SceneTranscriptService.updateSceneTranscript(authInfo, command);

            // then
            assertEquals(text, updatedTs.getText());
            assertEquals(speed, updatedTs.getProperty().speed());
        }

        @Test
        @DisplayName("003. 대본 지연을 수정할 수 있다.")
        void 대본_지연을_수정할_수_있다() {
            // stub
            var member = MemberFixture.create();
            var ts = SceneTranscriptFixture.create();
            var authInfo = new AuthInfo(member.getId());
            Mockito
                .when(sceneTranscriptRepository.findForUpdate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(ts));

            // when
            var text = "이것은 변경된 텍스트 입니다.";
            var speed = 2.0;
            var postDelay = 300;
            var command = new SceneTranscriptUpdateCommand(1L, 1L, 1L, text, speed, postDelay);

            // when
            var updatedTs = SceneTranscriptService.updateSceneTranscript(authInfo, command);

            // then
            assertEquals(text, updatedTs.getText());
            assertEquals(speed, updatedTs.getProperty().speed());
            assertEquals(postDelay, updatedTs.getProperty().postDelay());
        }

        @Test
        @DisplayName("004. 소유하지 않은 대본은 수정할 수 없다.")
        void 소유하지_않은_대본은_수정할_수_없다() {
            // stub
            var member = MemberFixture.create();
            var ts = SceneTranscriptFixture.create();
            var authInfo = new AuthInfo((long) 999);
            Mockito
                .when(sceneTranscriptRepository.findForUpdate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenAnswer((invoke) -> {
                    var memberId = invoke.getArgument(3);
                    if (memberId.equals(member.getId())) {
                        return Optional.of(ts);
                    }
                    return Optional.empty();
                });

            // when
            var text = "이것은 변경된 텍스트 입니다.";
            var speed = 2.0;
            var postDelay = 300;
            var command = new SceneTranscriptUpdateCommand(1L, 1L, 1L, text, speed, postDelay);

            // when
            assertThrows(CoreApiException.class, () -> SceneTranscriptService.updateSceneTranscript(authInfo, command));
        }
    }
}
