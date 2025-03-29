package org.cm.domain.taskscript;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.task.Task;
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;
import org.jspecify.annotations.NonNull;

@Getter
@Entity
@Table(name = "task_scripts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskScript extends BaseEntity {

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @Column(nullable = false, updatable = false)
    private Long sceneId;

    // 300자 제한이지만 넉넉하게 400자로 잡음
    @Column(nullable = false, length = 400)
    private String sentence;
    private String fileId;

    @Column(nullable = false, name = "task_script_options")
    private String option;

    @Column(length = 500)
    private String failMessage;

    @Column(nullable = false)
    @Convert(converter = TaskScriptStatus.Converter.class)
    private TaskScriptStatus status;


    public TaskScript(Task task, Long sceneId, String sentence, Object option) {
        this.task = task;
        this.sceneId = sceneId;
        this.sentence = sentence;
        this.status = TaskScriptStatus.PENDING;
        this.option = option.toString();
    }

    public TaskScript(Task task, Long sceneId, String sentence) {
        this.task = task;
        this.sceneId = sceneId;
        this.sentence = sentence;
        this.status = TaskScriptStatus.PENDING;
    }

    public TaskScript(Task task, String sentence) {
        this.task = task;
        this.sentence = sentence;
        this.status = TaskScriptStatus.PENDING;
    }

    public void start() {
        if (status != TaskScriptStatus.PENDING) {
            throw new CoreDomainException(CoreDomainExceptionCode.TASK_SCRIPT_START_ALLOWED_ONLY_IN_PENDING);
        }
        task.start();
        this.status = TaskScriptStatus.IN_PROGRESS;
    }

    public void complete(@NonNull String fileId) {
        if (status != TaskScriptStatus.IN_PROGRESS) {
            throw new CoreDomainException(CoreDomainExceptionCode.TASK_SCRIPT_SUCCESS_ALLOWED_ONLY_IN_PROGRESS);
        }
        this.fileId = fileId;
        this.status = TaskScriptStatus.SUCCESS;
    }

    public void cancel() {
        if (status != TaskScriptStatus.IN_PROGRESS) {
            throw new CoreDomainException(CoreDomainExceptionCode.TASK_SCRIPT_CANCEL_ALLOWED_PENDING_OR_IN_PROGRESS);
        }
    }

    public void fail(@NonNull String failMessage) {
        if (status != TaskScriptStatus.IN_PROGRESS) {
            throw new CoreDomainException(CoreDomainExceptionCode.TASK_SCRIPT_FAIL_ALLOWED_ONLY_IN_PROGRESS);
        }
        this.failMessage = failMessage;
    }


}
