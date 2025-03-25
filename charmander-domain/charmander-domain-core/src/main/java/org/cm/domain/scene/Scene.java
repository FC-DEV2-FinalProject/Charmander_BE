package org.cm.domain.scene;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.project.Project;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    private SceneMedia background;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private SceneMedia avatar;

    @Embedded
    @AttributeOverride(name = "background", column = @Column(name = "property_background"))
    private SceneProperty property;

    public Scene(Project project) {
        this.project = project;
    }
}
