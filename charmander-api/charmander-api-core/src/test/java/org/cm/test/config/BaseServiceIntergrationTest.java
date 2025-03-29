package org.cm.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.cm.domain.member.Member;
import org.cm.domain.project.Project;
import org.cm.domain.scene.Scene;
import org.cm.test.fixture.MemberFixture;
import org.cm.test.fixture.ProjectFixture;
import org.cm.test.fixture.SceneFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.IntStream;

@DataJpaTest
public abstract class BaseServiceIntergrationTest {
    @Autowired
    private EntityManager em;

    @Autowired(required = false)
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
        }
    }

    protected Member createMember() {
        var member = MemberFixture.create();
        return em.merge(member);
    }

    protected Project createProject(Member member) {
        var project = ProjectFixture.create(member);
        return em.merge(project);
    }

    protected Scene createScene(Project project) {
        var scene = SceneFixture.create(project);
        return em.merge(scene);
    }

    @SuppressWarnings("SameParameterValue")
    protected Project populatProjectData(Member member, int nScenes) {
        var project = ProjectFixture.create(member);
        var scenes = IntStream.range(0, nScenes)
            .mapToObj(i -> SceneFixture.create(project))
            .toList();
        scenes.forEach(project::addScene);
        return em.merge(project);
    }
}
