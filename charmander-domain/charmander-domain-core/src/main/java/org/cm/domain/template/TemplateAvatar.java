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
@Entity(name = "templateAvatar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateAvatar extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", updatable = false)
    private Member owner;

    @Column(nullable = false, updatable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private int priority = 1;

    @Column(nullable = false)
    private String fileUrl;

    @Convert(converter = TemplateAvatarType.Converter.class)
    @Column(nullable = false)
    private TemplateAvatarType type;

    @Embedded
    private ScreenSize size;

    private TemplateAvatar(
        Member owner,
        String name,
        TemplateAvatarType type,
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

    public static TemplateAvatar createUserOwned(
        Member owner,
        String name,
        TemplateAvatarType type,
        String fileUrl,
        ScreenSize size
    ) {
        return new TemplateAvatar(owner, name, type, fileUrl, size);
    }

    public static TemplateAvatar createShared(
        String name,
        TemplateAvatarType type,
        String fileUrl,
        ScreenSize size
    ) {
        return new TemplateAvatar(null, name, type, fileUrl, size);
    }
}
