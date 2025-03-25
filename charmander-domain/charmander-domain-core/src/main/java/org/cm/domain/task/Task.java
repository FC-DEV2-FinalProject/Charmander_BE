package org.cm.domain.task;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.project.Project;
import org.cm.domain.taskscript.TaskScript;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

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
    private TaskStatus status = TaskStatus.PENDING;

    // TODO 삭제 예정
    @Column
    @Deprecated
    private String jobId;

    @JdbcTypeCode(SqlTypes.JSON)
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

    public void start() {
        if (status == TaskStatus.IN_PROGRESS) {
            return;
        }
        if (status != TaskStatus.PENDING) {
            throw new CoreDomainException(CoreDomainExceptionCode.START_ALLOWED_ONLY_IN_PENDING);
        }
        this.status = TaskStatus.IN_PROGRESS;
    }

    public void succeed(TaskOutput output) {
        if (status != TaskStatus.IN_PROGRESS) {
            throw new CoreDomainException(CoreDomainExceptionCode.SUCCEED_ALLOWED_ONLY_IN_PROGRESS);
        }
        this.output = output;
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

    public void tryConvert() {
        status = TaskStatus.CONVERTING;
    }
}
