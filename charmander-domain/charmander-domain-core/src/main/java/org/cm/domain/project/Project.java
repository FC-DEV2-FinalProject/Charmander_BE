package org.cm.domain.project;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.member.Member;

import java.time.LocalDateTime;

@Getter
@Entity(name = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private Member owner;

    @Setter
    @Column(nullable = false)
    private String name;

    @Convert(converter = ProjectStatusConverter.class)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.DRAFT;

    @Setter
    @Convert(converter = ProjectData.Converter.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private ProjectData data;

    @Version
    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private LocalDateTime lastAccessedAt = LocalDateTime.now();

    public Project(
        Member owner,
        String name,
        ProjectStatus status,
        ProjectData data,
        int version
    ) {
        this.owner = owner;
        this.name = name;
        this.status = status;
        this.data = data;
        this.version = version;
    }

    public static Project newDraftProject(Member owner) {
        return new Project(owner, "New Project", ProjectStatus.DRAFT, ProjectData.empty(), 0);
    }
}
