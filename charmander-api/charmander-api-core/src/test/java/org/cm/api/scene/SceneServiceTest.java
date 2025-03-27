package org.cm.api.scene;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.cm.api.scene.dto.SceneUpdateRequest;
import org.cm.domain.member.Member;
import org.cm.domain.project.Project;
import org.cm.domain.scene.Scene;
import org.cm.security.AuthInfo;
import org.cm.test.fixture.MemberFixture;
import org.cm.test.fixture.ProjectFixture;
import org.cm.test.fixture.SceneFixture;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Import({
  SceneService.class,
  SceneUpdateRequest.Mapper.class
})
@DataJpaTest
class SceneServiceTest {
    @Autowired
    SceneService sceneService;

    @Autowired
    EntityManager em;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @AfterEach
    void tearDown() {
        objectMapper = null;
    }

    @Nested
    @DisplayName("[조회 테스트]")
    class GetTest {
        @Test
        @DisplayName("001. 사용자가 소유한 프로젝트를 조회할 수 있어야 함.")
        void test00001() {
            // given
            var member1 = createMember();
            var member2 = createMember();
            var project1 = populatProjectData(member1, 3);
            var authInfo = new AuthInfo(member1.getId());

            // when
            var scenes = sceneService.getProjectScenes(authInfo, project1.getId());

            // then
            assertEquals(project1.getScenes().size(), scenes.size());
        }

        @Test
        @DisplayName("002. 다른 사용자가 소유한 프로젝트를 조회할 수 없어야 함.")
        void test00002() {
            // given
            var member1 = createMember();
            var member2 = createMember();
            var project1 = populatProjectData(member1, 3);
            var authInfo = new AuthInfo(member2.getId());

            // when
            var scenes = sceneService.getProjectScenes(authInfo, project1.getId());

            // then
            assertEquals(0, scenes.size());
        }
    }

    @Nested
    @DisplayName("[삽입 테스트]")
    class InsertionTest {
        @Test
        @DisplayName("001. 새로운 Scene을 생성할 수 있어야 함.")
        void test00001() {
            // given
            var member = createMember();
            var project = populatProjectData(member, 0);
            var authInfo = new AuthInfo(member.getId());

            // when
            var scene = sceneService.createScene(authInfo, project.getId());
            var scenes = sceneService.getProjectScenes(authInfo, project.getId());

            // then
            assertNotNull(scene.getId());
            assertEquals(1, scenes.size());
        }
    }

    @Nested
    @DisplayName("[수정 테스트]")
    class UpdateTest {
        @Test
        @DisplayName("001. 자막 테스트를 수정할 수 있어야 함.")
        void test00001() throws Exception {
            // given
            var member = createMember();
            var project = createProject(member);
            var scene = createScene(project);
            var authInfo = new AuthInfo(member.getId());

            var request = new SceneUpdateRequest(
              new SceneUpdateRequest.SceneSubtitleDto("new subtitle", null),
              null
            );

            // when
            var before = objectMapper.readValue(objectMapper.writeValueAsString(scene), Scene.class);
            sceneService.updateScene(authInfo, project.getId(), scene.getId(), request);
            var after = sceneService.getProjectScenes(authInfo, project.getId()).getFirst();

            // then
            assertNotEquals(before, after);
            assertEquals(request.subtitle().text(), after.getSubtitle().getText());
            assertNotEquals(before.getSubtitle().getText(), after.getSubtitle().getText());
        }
    }

    @Nested
    @DisplayName("[삭제 테스트]")
    class DeletionTest {
        @Test
        @DisplayName("001. 사용자가 소유한 Scene을 삭제할 수 있어야 함.")
        void test00001() {
            // given
            var member = createMember();
            var project = populatProjectData(member, 1);
            var scene = project.getScenes().getFirst();
            var authInfo = new AuthInfo(member.getId());

            // when
            sceneService.deleteScene(authInfo, project.getId(), scene.getId());
            var scenes = sceneService.getProjectScenes(authInfo, project.getId());

            // then
            assertEquals(0, scenes.size());
        }

        @Test
        @DisplayName("002. 다른 사용자가 소유한 Scene을 삭제할 수 없어야 함.")
        void test00002() {
            // given
            var member1 = createMember();
            var member2 = createMember();
            var project = populatProjectData(member1, 1);
            var scene = project.getScenes().getFirst();
            var authInfo = new AuthInfo(member2.getId());

            // when
            assertThrows(Exception.class, () -> sceneService.deleteScene(authInfo, project.getId(), scene.getId()));
        }
    }

    private Member createMember() {
        var member = MemberFixture.create();
        return em.merge(member);
    }

    private Project createProject(Member member) {
        var project = ProjectFixture.create(member);
        return em.merge(project);
    }

    private Scene createScene(Project project) {
        var scene = SceneFixture.create(project);
        return em.merge(scene);
    }

    @SuppressWarnings("SameParameterValue")
    private Project populatProjectData(Member member, int nScenes) {
        var project = ProjectFixture.create(member);
        var scenes = IntStream.range(0, nScenes)
            .mapToObj(i -> SceneFixture.create(project))
            .toList();
        scenes.forEach(project::addScene);
        return em.merge(project);
    }
}
