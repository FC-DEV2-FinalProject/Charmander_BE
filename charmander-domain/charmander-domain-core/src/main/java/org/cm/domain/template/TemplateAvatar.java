package org.cm.domain.template;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cm.domain.common.BaseEntity;

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

    public TemplateAvatar(
        String name,
        String fileUrl
    ) {
        this.name = name;
        this.fileUrl = fileUrl;
    }
}
