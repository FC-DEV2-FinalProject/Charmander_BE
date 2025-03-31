package org.cm.api.project;

import org.cm.api.task.TaskService;
import org.cm.repository.NamedLockTemplate;
import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceIntergrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@Import({
    ProjectService.class
})
@DisplayName("[통합 테스트] ProjectService")
class ProjectServiceIntergrationTest extends BaseServiceIntergrationTest {
    @Autowired
    ProjectService projectService;

    @MockitoBean
    TaskService taskService;

    @MockitoBean
    NamedLockTemplate namedLockTemplate;

    @Nested
    @DisplayName("[삽입]")
    class InsertionTest {
        @Test
        @DisplayName("001. Project 생성 시 최소 1개의 Scene이 존재해야 한다.")
        void 최소_1개의_Scene이_존재해야_한다() {
            // given
            var member = createMember();
            var authInfo = new AuthInfo(member.getId());

            // when
            var project = projectService.createProject(authInfo);
            var scenes = project.getScenes();

            // then
            assertFalse(scenes.isEmpty());
        }
    }
}
