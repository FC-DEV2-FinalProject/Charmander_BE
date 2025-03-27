package org.cm.domain.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.converter.SceneConverter;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.task.TaskInputSchema.Scene;

@Getter
@Entity
@Table(name = "scene_outputs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SceneOutput extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private Long sceneId;

    @Column(nullable = false, updatable = false)
    private Long taskId;

    @Convert(converter = SceneConverter.class)
    @Column(columnDefinition = "MEDIUMTEXT")
    private Scene scene;

    private String sceneFileId;
    private String resourceFileId;
    private String voiceFileId;
    private String avatarFileId;


    public SceneOutput(Long sceneId, Long taskId, Scene scene) {
        this.sceneId = sceneId;
        this.taskId = taskId;
        this.scene = scene;
    }

    public void update(String combineAudioId) {
        this.voiceFileId = voiceFileId;
    }

    public void updateSceneId(String sceneFileId) {
        this.sceneFileId = sceneFileId;
    }
}
