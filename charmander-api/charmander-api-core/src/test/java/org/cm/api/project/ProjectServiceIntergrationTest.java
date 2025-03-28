package org.cm.api.project;

import org.cm.api.task.TaskService;
import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceIntergrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;


@Import(ProjectService.class)
@DisplayName("[통합 테스트] ProjectService")
class ProjectServiceIntergrationTest extends BaseServiceIntergrationTest {
    @Autowired
    ProjectService projectService;

    @MockitoBean
    TaskService taskService;

    @Nested
    @DisplayName("[삽입]")
    class InsertionTest {
        @Test
        @DisplayName("Project 추가 후 Scene이 최소 1개는 존재해야 한다.")
        public void Project_추가_후_최소_한_개의_Scene이_존재해야_한다() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);

            // when
            var project = projectService.createProject(authInfo);

            // then
            assertNotEquals(0, project.getScenes().size());
        }
    }
}
