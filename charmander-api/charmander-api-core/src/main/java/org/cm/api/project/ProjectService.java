package org.cm.api.project;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.cm.api.project.dto.ProjectUpdateRequest;
import org.cm.domain.member.MemberRepository;
import org.cm.domain.project.Project;
import org.cm.domain.project.ProjectRepository;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Project> getAllProjects(AuthInfo authInfo) {
        return projectRepository.findAllByOwnerId(authInfo.getMemberId());
    }

    @Transactional(readOnly = true)
    public Project getProjectById(AuthInfo authInfo, Long id) {
        return Optional.ofNullable(projectRepository.findByIdAndOwnerId(id, authInfo.getMemberId()))
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
    }

    public Project createProject(AuthInfo authInfo) {
        var member = memberRepository.findById(authInfo.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        var project = Project.newDraftProject(member);
        return projectRepository.save(project);
    }

    public Project updateProject(AuthInfo authInfo, Long id, ProjectUpdateRequest updateRequest) {
        var existingProject = Optional.ofNullable(projectRepository.findByIdAndOwnerIdForUpdate(id, authInfo.getMemberId()))
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
        updateRequest.update(existingProject);
        return projectRepository.save(existingProject);
    }

    public void deleteProject(AuthInfo authInfo, Long id) {
        var project = Optional.ofNullable(projectRepository.findByIdAndOwnerId(id, authInfo.getMemberId()))
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.PROJECT_NOT_FOUND));
        projectRepository.delete(project);
    }
}
