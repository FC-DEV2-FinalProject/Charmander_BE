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
        @DisplayName("001. name이 Null일 경우 변경되지 않아야 함")
        public void 값이_NULL일_경우_변경되지_않아야_함() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);
            var projectId = 1L;
            var request = new ProjectUpdateRequest(
                null, null
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
                "new name", null
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

        @Test
        @DisplayName("003. width가 Null일 경우 변경되지 않아야 함")
        public void width가_NULL일_경우_변경되지_않아야_함() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);
            var projectId = 1L;
            var request = new ProjectUpdateRequest(
                null, new ProjectUpdateRequest.ProjectPropertyDto(
                new ProjectUpdateRequest.ProjectPropertyDto.ScreenSizeDto(null, 200)
            )
            );
            var project = ProjectFixture.create();
            Mockito
                .when(projectRepository.findByIdAndOwnerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(project));

            // when
            var prevWidth = project.getProperty().screenSize().width();
            projectService.updateProject(authInfo, projectId, request);
            var afterWidth = project.getProperty().screenSize().width();

            // then
            assertThat(request.property()).isNotNull();
            assertThat(request.property().screenSize()).isNotNull();
            assertThat(request.property().screenSize().width()).isNull();
            assertThat(prevWidth).isEqualTo(afterWidth);
        }

        @Test
        @DisplayName("004. height이 Null일 경우 변경되지 않아야 함")
        public void height가_NULL일_경우_변경되지_않아야_함() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);
            var projectId = 1L;
            var request = new ProjectUpdateRequest(
                null, new ProjectUpdateRequest.ProjectPropertyDto(
                new ProjectUpdateRequest.ProjectPropertyDto.ScreenSizeDto(100, null)
            )
            );
            var project = ProjectFixture.create();
            Mockito
                .when(projectRepository.findByIdAndOwnerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(project));

            // when
            var prevHeight = project.getProperty().screenSize().height();
            projectService.updateProject(authInfo, projectId, request);
            var afterHeight = project.getProperty().screenSize().height();

            // then
            assertThat(request.property()).isNotNull();
            assertThat(request.property().screenSize()).isNotNull();
            assertThat(request.property().screenSize().height()).isNull();
            assertThat(prevHeight).isEqualTo(afterHeight);
        }

        @Test
        @DisplayName("005. width를 변경할 수 있어야 함")
        public void width를_변경할_수_있어야_함() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);
            var projectId = 1L;
            var request = new ProjectUpdateRequest(
                null, new ProjectUpdateRequest.ProjectPropertyDto(
                new ProjectUpdateRequest.ProjectPropertyDto.ScreenSizeDto(100, 200)
            )
            );
            var project = ProjectFixture.create();
            Mockito
                .when(projectRepository.findByIdAndOwnerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(project));

            // when
            var prevWidth = project.getProperty().screenSize().width();
            projectService.updateProject(authInfo, projectId, request);
            var afterWidth = project.getProperty().screenSize().width();

            // then
            assertThat(request.property()).isNotNull();
            assertThat(request.property().screenSize()).isNotNull();
            assertThat(prevWidth).isNotEqualTo(request.property().screenSize().width());
            assertThat(prevWidth).isNotEqualTo(afterWidth);
            assertThat(request.property().screenSize().width()).isEqualTo(afterWidth);
        }

        @Test
        @DisplayName("006. height를 변경할 수 있어야 함")
        public void height를_변경할_수_있어야_함() {
            // given
            var authInfo = Mockito.mock(AuthInfo.class);
            var projectId = 1L;
            var request = new ProjectUpdateRequest(
                null, new ProjectUpdateRequest.ProjectPropertyDto(
                new ProjectUpdateRequest.ProjectPropertyDto.ScreenSizeDto(100, 200)
            )
            );
            var project = ProjectFixture.create();
            Mockito
                .when(projectRepository.findByIdAndOwnerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(project));

            // when
            var prevHeight = project.getProperty().screenSize().height();
            projectService.updateProject(authInfo, projectId, request);
            var afterHeight = project.getProperty().screenSize().height();

            // then
            assertThat(request.property()).isNotNull();
            assertThat(request.property().screenSize()).isNotNull();
            assertThat(prevHeight).isNotEqualTo(request.property().screenSize().height());
            assertThat(prevHeight).isNotEqualTo(afterHeight);
            assertThat(request.property().screenSize().height()).isEqualTo(afterHeight);
        }
    }
}
