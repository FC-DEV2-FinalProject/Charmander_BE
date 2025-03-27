package org.cm.domain.task;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.converter.InputSchemaConverter;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.project.Project;
import org.cm.domain.taskscript.TaskScript;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;

@Getter
@Entity(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private Project project;

    @Convert(converter = TaskType.Converter.class)
    @Column(nullable = false)
    private TaskType type;

    @Convert(converter = TaskStatus.Converter.class)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.CREATING;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskScript> taskScripts;

    // TODO 삭제 예정
    @Column
    private String jobId;

    @Convert(converter = InputSchemaConverter.class)
    @Column(nullable = false, updatable = false, columnDefinition = "MEDIUMTEXT")
    private TaskInputSchema inputSchema;

    @Embedded
    private TaskOutput output;

    @Column(nullable = false)
    private int retryCount = 0;

    // TODO 생성자에서 PENDING으로 바꾸기
    public Task(Project project, TaskType type) {
        this.project = project;
        this.type = type;
    }

    @PrePersist
    public void prePersist() {
        if (status == TaskStatus.CREATING) {
            status = TaskStatus.PENDING;
        }
    }

    public void setTaskScripts(List<TaskScript> taskScripts) {
        if (status != TaskStatus.CREATING) {
            throw new CoreDomainException(CoreDomainExceptionCode.TASK_SCRIPTS_ALLOWED_ONLY_CREATING);
        }
        this.taskScripts = taskScripts;
    }

    public void start() {
        if (status == TaskStatus.IN_PROGRESS) {
            return;
        }
        if (status != TaskStatus.PENDING) {
            throw new CoreDomainException(CoreDomainExceptionCode.START_ALLOWED_ONLY_IN_PENDING);
        }
        this.status = TaskStatus.IN_PROGRESS;
    }

    public void succeed() {
        if (status != TaskStatus.IN_PROGRESS) {
            throw new CoreDomainException(CoreDomainExceptionCode.SUCCEED_ALLOWED_ONLY_IN_PROGRESS);
        }
        this.status = TaskStatus.SUCCESS;
    }

    public void retry() {
        if (status != TaskStatus.FAILED) {
            throw new CoreDomainException(CoreDomainExceptionCode.RETRY_ALLOWED_ONLY_IN_FAILED);
        }
        status = TaskStatus.PENDING;
        retryCount++;
    }

    public void cancel() {
        switch (status) {
            case PENDING, IN_PROGRESS -> status = TaskStatus.CANCELED;
            default -> throw new CoreDomainException(CoreDomainExceptionCode.CANCEL_ALLOWED_PENDING_OR_IN_PROGRESS);
        }
    }

    public void tryConvert(String jobId) {
        status = TaskStatus.CONVERTING;
        this.jobId = jobId;
    }

    public void fail(String message) {

    }
}