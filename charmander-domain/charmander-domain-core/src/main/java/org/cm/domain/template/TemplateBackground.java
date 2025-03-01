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
@Entity(name = "templateBackground")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateBackground extends BaseEntity {
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

    public TemplateBackground(
            String name,
            TemplateBackgroundType type,
            String fileUrl,
            ScreenSize size
    ) {
        this.name = name;
        this.type = type;
        this.fileUrl = fileUrl;
        this.size = size;
    }
}
