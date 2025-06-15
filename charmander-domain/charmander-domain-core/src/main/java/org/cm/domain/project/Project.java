package org.cm.domain.project;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.member.Member;
import org.cm.domain.scene.Scene;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity(name = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private Member owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Scene> scenes = new HashSet<>();

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Embedded
    private ProjectProperty property;

    private String newsArticle;

    @Version
    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private LocalDateTime lastAccessedAt = LocalDateTime.now();

    public Project(
        Member owner,
        String name,
        int version
    ) {
        this.owner = owner;
        this.name = name;
        this.property = ProjectProperty.createDefault();
        this.version = version;
    }

    public void addScene(Scene scene) {
        scenes.add(scene);
    }

    public void removeScene(Scene scene) {
        scenes.remove(scene);
    }

    public static Project newCreateProject(Member owner) {
        return new Project(owner, "New Project", 0);
    }

    public void updateProjectNewsArticle(String newsArticle) {
        this.newsArticle = newsArticle;
    }
}
