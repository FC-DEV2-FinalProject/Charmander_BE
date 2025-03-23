package org.cm.domain.scene;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.project.Project;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scene extends BaseEntity {
    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Project project;

    @OneToMany
    private List<SceneTranscript> transcripts;

    @Embedded
    private SceneSubtitle subtitle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private List<SceneMedia> media;

    @Embedded
    private SceneProperty property;

    public Scene(Project project) {
        this.project = project;
    }
}
