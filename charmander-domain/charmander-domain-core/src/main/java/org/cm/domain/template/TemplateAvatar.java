package org.cm.domain.template;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.common.ScreenSize;

@Getter
@Entity(name = "templateAvatar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateAvatar extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private int priority = 1;

    @Column(nullable = false, updatable = false)
    private String fileUrl;

    @Convert(converter = TemplateAvatarType.Converter.class)
    @Column(nullable = false)
    private TemplateAvatarType type;

    @Embedded
    private ScreenSize size;

    public TemplateAvatar(
        String name,
        TemplateAvatarType type,
        String fileUrl,
        ScreenSize size
    ) {
        this.name = name;
        this.fileUrl = fileUrl;
        this.type = type;
        this.size = size;
    }
}
