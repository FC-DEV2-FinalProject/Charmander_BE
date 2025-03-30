package org.cm.domain.scene;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.project.Project;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scene extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Project project;

    @OneToMany(mappedBy = "scene", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SceneTranscript> transcripts = new HashSet<>();

    @Setter
    @Embedded
    private SceneSubtitle subtitle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private SceneMedia background;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private SceneMedia avatar;

    @Setter
    @Embedded
    @AttributeOverride(name = "background", column = @Column(name = "property_background"))
    private SceneProperty property;

    public void addTranscript(SceneTranscript transcript) {
        this.transcripts.add(transcript);
    }

    public Scene(
        Project project,
        SceneSubtitle subtitle,
        SceneMedia background,
        SceneMedia avatar,
        SceneProperty property
    ) {
        this.project = project;
        this.subtitle = subtitle;
        this.background = background;
        this.avatar = avatar;
        this.property = property;
    }

    public static Scene createEmpty(Project project) {
        return new Scene(project, SceneSubtitle.createDefault(), SceneMedia.createDefault(), SceneMedia.createDefault(), SceneProperty.createDefault());
    }
}
