package org.cm.api.project;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cm.api.project.dto.ProjectUpdateRequest;
import org.cm.api.task.TaskService;
import org.cm.domain.member.MemberRepository;
import org.cm.domain.project.Project;
import org.cm.domain.project.ProjectRepository;
import org.cm.domain.task.Task;
import org.cm.domain.task.TaskType;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.cm.repository.NamedLockTemplate;
import org.cm.security.AuthInfo;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private final NamedLockTemplate namedLockTemplate;

    @Transactional(readOnly = true)
    public List<Project> getAllProjects(AuthInfo authInfo) {
        return projectRepository.findAllByOwnerId(authInfo.getMemberId());
    }

    public Project getProjectByIdForDetails(AuthInfo authInfo, Long id) {
        return projectRepository.findByIdAndOwnerIdWithFetch(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
    }

    public Project createProject(AuthInfo authInfo) {
        var member = memberRepository.findById(authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.MEMBER_NOT_FOUND));

        var project = Project.newCreateProject(member);
        return projectRepository.save(project);
    }

    public void updateProject(AuthInfo authInfo, Long id, ProjectUpdateRequest request) {
        var project = projectRepository.findByIdAndOwnerId(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));

        ProjectUpdateRequest.update(project, request);
    }

    public void deleteProject(AuthInfo authInfo, Long id) {
        var project = projectRepository.findByIdAndOwnerId(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
        projectRepository.delete(project);
    }

    public void modifyProjectNewsArticle(Long id, Long memberId, String newArticle) {
        var lockName = "Project-" + id;
        namedLockTemplate
            .runWithNamedLockFallback(lockName, () -> {
                Project foundProject = projectRepository
                    .findByIdAndOwnerIdForUpdate(id, memberId)
                    .orElseThrow(() -> new CoreDomainException(CoreDomainExceptionCode.NOT_FOUND_PROJECT));

                LocalDateTime timestamp = LocalDateTime.now();

                if (!foundProject.getUpdatedAt().isEqual(timestamp) && foundProject.getUpdatedAt().isAfter(timestamp)) {
                    throw new OptimisticLockingFailureException("동시성 충돌: 다른 사용자가 이미 수정했습니다.");
                }

                foundProject.updateProjectNewsArticle(newArticle);
                projectRepository.save(foundProject);
        });
    }

    public Task generateProject(@NonNull AuthInfo authInfo, Long id) {
        var project = projectRepository.findByIdAndOwnerIdWithFetch(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
        return taskService.createTask(project, TaskType.VIDEO);
    }
}
