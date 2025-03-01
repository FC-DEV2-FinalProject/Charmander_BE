package org.cm.domain.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.project.Project;

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

    @Embedded
    private TaskOutput output;

    @Column(nullable = false)
    private int retryCount = 0;

    public Task(Project project, TaskType type) {
        this.project = project;
        this.type = type;
    }

    public void succeed(TaskOutput output) {
        if (status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("invalid task status");
        }
        this.output = output;
        this.status = TaskStatus.SUCCESS;
    }

    public void retry() {
        if (status != TaskStatus.FAILED) {
            throw new IllegalStateException("invalid task status");
        }
        status = TaskStatus.PENDING;
        retryCount++;
    }

    public void cancel() {
        if (status != TaskStatus.PENDING) {
            throw new IllegalStateException("invalid task status");
        }
        status = TaskStatus.CANCELED;
    }
}
