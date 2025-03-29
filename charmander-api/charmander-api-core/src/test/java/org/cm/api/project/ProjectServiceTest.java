package org.cm.api.project;

import org.cm.api.project.dto.ProjectUpdateRequest;
import org.cm.api.task.TaskService;
import org.cm.domain.member.MemberRepository;
import org.cm.domain.project.ProjectRepository;
import org.cm.security.AuthInfo;
import org.cm.test.fixture.ProjectFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith({MockitoExtension.class})
class ProjectServiceTest {
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


    }
}
