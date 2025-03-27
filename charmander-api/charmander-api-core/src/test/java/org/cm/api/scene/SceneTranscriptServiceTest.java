package org.cm.api.scene;

import org.cm.api.scene.dto.SceneTranscriptUpdateCommand;
import org.cm.domain.scene.SceneTranscriptRepository;
import org.cm.security.AuthInfo;
import org.cm.test.fixture.MemberFixture;
import org.cm.test.fixture.SceneTranscriptFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SceneTranscriptServiceTest {
    @InjectMocks
    SceneTranscriptService SceneTranscriptService;

    @Mock
    SceneTranscriptRepository sceneTranscriptRepository;

    @BeforeEach
    void setUp() {
        Mockito
            .when(sceneTranscriptRepository.save(Mockito.any()))
            .thenAnswer((invoke) -> invoke.getArgument(0));
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(sceneTranscriptRepository);
    }

    @Nested
    @DisplayName("[수정]")
    class UpdateTest {
        @Test
        @DisplayName("001. 대본 `text`가 수정 가능해야 한다.")
        void test00001() {
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
        @DisplayName("002. 대본 `speed`가 수정 가능해야 한다.")
        void test00002() {
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
        @DisplayName("002. 대본 `postDelay`가 수정 가능해야 한다.")
        void test00003() {
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
    }
}
