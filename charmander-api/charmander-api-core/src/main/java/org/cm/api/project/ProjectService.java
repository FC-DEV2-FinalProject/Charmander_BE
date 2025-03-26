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
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.cm.security.AuthInfo;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

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

    // project newsArticle
    // modify
    @Transactional
    public void modifyProjectNewsArticle(Long id, Long memberId, String newArticle, LocalDateTime timestamp) {
        int retryCount = 0;
        int maxRetries = 5;

        while (retryCount < maxRetries) {
            try {
                Project foundProject = projectRepository
                    .findByIdAndOwnerIdForUpdate(id, memberId)
                    .orElseThrow(() -> new CoreDomainException(CoreDomainExceptionCode.NOT_FOUND_PROJECT));

                // db에 저장된 updateAt보다 timestamp가 최신 정보를 가지고 있다면 충돌 발생
                if (foundProject.getUpdatedAt().isAfter(timestamp)) {
                    throw new OptimisticLockingFailureException("동시성 충돌: 다른 사용자가 이미 수정했습니다.");
                }

                foundProject.updateProjectNewsArticle(newArticle);
                // DB에서의 최신 timestamp 자동 갱신
                projectRepository.save(foundProject);

                break; // 성공적으로 업데이트 완료

            } catch (OptimisticLockingFailureException e) {
                retryCount++;
                logger.warn("데이터 충돌 발생 {}", retryCount);

                // 잠시 대기 후 재시도
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                if (retryCount >= maxRetries) {
                    logger.info("동시성 5회 이상 충돌 ");
                    logger.info("네임드 락 시작");

                    projectRepository.getLock(id.toString());
                    projectRepository.updateProjectNewsArticleFindById(id, newArticle, timestamp);
                    logger.info("네임드 락 종료");
                    return;
                }
            }finally {
                projectRepository.releaseLock(id.toString());
            }
        }// end while
    }// end method

    public Task generateProject(@NonNull AuthInfo authInfo, Long id) {
        var project = projectRepository.findByIdAndOwnerIdWithFetch(id, authInfo.getMemberId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
        return taskService.createTask(project, TaskType.VIDEO);
    }
}
