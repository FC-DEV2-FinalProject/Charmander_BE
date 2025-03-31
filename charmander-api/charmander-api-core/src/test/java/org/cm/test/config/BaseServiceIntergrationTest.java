package org.cm.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.common.ScreenSize;
import org.cm.domain.file.UploadedFile;
import org.cm.domain.member.Member;
import org.cm.domain.project.Project;
import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneTranscript;
import org.cm.domain.template.TemplateAvatar;
import org.cm.domain.template.TemplateAvatarType;
import org.cm.domain.template.TemplateBackground;
import org.cm.domain.template.TemplateBackgroundType;
import org.cm.test.fixture.*;
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

    protected SceneTranscript createSceneTranscript(Scene scene) {
        var transcript = SceneTranscriptFixture.create(scene);
        return em.merge(transcript);
    }

    protected TemplateBackground createTemplateBackground(Member member) {
        TemplateBackground bg;
        if (member == null) {
            bg = TemplateBackground.createShared("1", TemplateBackgroundType.Image, "1", ScreenSize.createDefault());
        }
        else {
            bg = TemplateBackground.createUserOwned(member, "1", TemplateBackgroundType.Image, "1", ScreenSize.createDefault());
        }
        return em.merge(bg);
    }

    protected TemplateAvatar createTemplateAvatar(Member member) {
        TemplateAvatar avatar;
        if (member == null) {
            avatar = TemplateAvatar.createShared("1", TemplateAvatarType.Image, "1", ScreenSize.createDefault());
        }
        else {
            avatar = TemplateAvatar.createUserOwned(member, "1", TemplateAvatarType.Image, "1", ScreenSize.createDefault());
        }
        return em.merge(avatar);
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

    protected UploadedFile createUploadedFile(Member member) {
        var uploadId = RandomKeyGenerator.generateRandomKey();
        var fileId = RandomKeyGenerator.generateRandomKey();
        var uploadedFile = UploadedFileFixture.create(uploadId, fileId, member);
        return em.merge(uploadedFile);
    }
}
