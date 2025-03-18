package org.cm.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.cm.TestContext;
import org.cm.domain.member.MemberRepository;
import org.cm.domain.project.ProjectRepository;
import org.cm.domain.task.TaskRepository;
import org.cm.domain.task.TaskStatus;
import org.cm.fixture.MemberFixture;
import org.cm.fixture.ProjectFixture;
import org.cm.fixture.TaskFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TaskServiceTest extends TestContext {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @Test
    void 영상_생성_요청_전_상태가_시작_상태로_변경된다(){
        var member = memberRepository.save(MemberFixture.create());
        var project = projectRepository.save(ProjectFixture.create(member));
        var task = taskRepository.save(TaskFixture.create(project));

        taskService.start(task.getId());

        var actual = taskRepository.getById(task.getId());
        assertThat(actual.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }
}