package org.cm.api.project;

import org.cm.api.project.dto.ProjectUpdateRequest;
import org.cm.api.task.TaskService;
import org.cm.domain.member.MemberRepository;
import org.cm.domain.project.ProjectRepository;
import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceUnitTest;
import org.cm.test.fixture.ProjectFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProjectServiceTest extends BaseServiceUnitTest {
    @InjectMocks
    ProjectService projectService;

    @Mock
    TaskService taskService;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    MemberRepository memberRepository;

    @Nested
    @DisplayName("[변경 테스트]")
    class UpdateTest {
        @Test
        @DisplayName("001. name: 값이 Null일 경우 변경되지 않아야 함")
        public void 값이_NULL일_경우_변경되지_않아야_함() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);
            var projectId = 1L;
            var request = new ProjectUpdateRequest(
                null
            );
            var project = ProjectFixture.create();
            Mockito
                .when(projectRepository.findByIdAndOwnerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(project));

            // when
            var prevName = project.getName();
            projectService.updateProject(authInfo, projectId, request);
            var afterName = project.getName();

            // then
            assertThat(request.name()).isNull();
            assertThat(prevName).isNotEqualTo(request.name());
            assertThat(prevName).isEqualTo(afterName);
        }

        @Test
        @DisplayName("002. name을 변경할 수 있어야 함")
        public void name을_변경할_수_있어야_함() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);
            var projectId = 1L;
            var request = new ProjectUpdateRequest(
                "new name"
            );
            var project = ProjectFixture.create();
            Mockito
                .when(projectRepository.findByIdAndOwnerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(project));

            // when
            var prevName = project.getName();
            projectService.updateProject(authInfo, projectId, request);
            var afterName = project.getName();

            // then
            assertThat(request.name()).isNotNull();
            assertThat(prevName).isNotEqualTo(request.name());
            assertThat(prevName).isNotEqualTo(afterName);
            assertThat(request.name()).isEqualTo(afterName);
        }
    }
}
