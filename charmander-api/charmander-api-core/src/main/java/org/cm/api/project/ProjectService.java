package org.cm.api.project;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cm.api.task.TaskService;
import org.cm.domain.member.MemberRepository;
import org.cm.domain.project.Project;
import org.cm.domain.project.ProjectRepository;
import org.cm.domain.task.Task;
import org.cm.domain.task.TaskType;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.security.AuthInfo;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ApplicationEventPublisherAware {
    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    private final TaskService taskService;

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Project> getAllProjects(AuthInfo authInfo) {
        return projectRepository.findAllByOwnerId(authInfo.getMemberId());
    }

    @Transactional(readOnly = true)
    public Project getProjectById(AuthInfo authInfo, Long id) {
        return projectRepository.findByIdAndOwnerId(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
    }

    public Project createProject(AuthInfo authInfo) {
        var member = memberRepository.findById(authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.MEMBER_NOT_FOUND));

        var project = Project.newCreateProject(member);
        return projectRepository.save(project);
    }

    public void deleteProject(AuthInfo authInfo, Long id) {
        var project = projectRepository.findByIdAndOwnerId(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
        projectRepository.delete(project);
    }

    public Task generateProject(@NonNull AuthInfo authInfo, Long id) {
        var project = projectRepository.findByIdAndOwnerIdWithFetch(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
        return taskService.createTask(project, TaskType.VIDEO);
    }
}
