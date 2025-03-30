package org.cm.domain.template;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cm.common.domain.SceneMediaType;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.common.ScreenSize;
import org.cm.domain.member.Member;
import org.cm.domain.scene.SceneMedia;

import java.time.Duration;

@Getter
@Entity(name = "templateBackground")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateBackground extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", updatable = false)
    private Member owner;

    @Column(nullable = false, updatable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private int priority = 1;

    @Convert(converter = TemplateBackgroundType.Converter.class)
    @Column(nullable = false)
    private TemplateBackgroundType type;

    @Column(nullable = false)
    private String fileUrl;

    @Embedded
    private ScreenSize size;

    TemplateBackground(
        Member owner,
        String name,
        TemplateBackgroundType type,
        String fileUrl,
        ScreenSize size
    ) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.fileUrl = fileUrl;
        this.size = size;
    }

    public SceneMedia toSceneMedia() {
        return new SceneMedia(
            SceneMediaType.Image,
            this.fileUrl,
            "0",
            Duration.ZERO,
            SceneMedia.Property.of(size)
        );
    }

    public static TemplateBackground createUserOwned(
        Member owner,
        String name,
        TemplateBackgroundType type,
        String fileUrl,
        ScreenSize size
    ) {
        return new TemplateBackground(owner, name, type, fileUrl, size);
    }

    public static TemplateBackground createShared(
        String name,
        TemplateBackgroundType type,
        String fileUrl,
        ScreenSize size
    ) {
        return new TemplateBackground(null, name, type, fileUrl, size);
    }
}
