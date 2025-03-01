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

@Getter
@Entity(name = "template")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template extends BaseEntity {
    @Convert(converter = TemplateStatus.Converter.class)
    @Column(nullable = false)
    private TemplateStatus status = TemplateStatus.DRAFT;

    @Setter
    @Column(nullable = false)
    private int priority = 1;

    @Embedded
    private TemplateData data;

    public Template(TemplateData data) {
        this.data = data;
    }

    public void update(TemplateData data) {
        if (status != TemplateStatus.DRAFT) {
            throw new IllegalStateException("초안 상태에서만 수정할 수 있음.");
        }
        this.data = data;
    }

    public void publish() {
        if (status != TemplateStatus.DRAFT) {
            throw new IllegalStateException("초안 상태에서만 공개할 수 있음.");
        }
        this.status = TemplateStatus.PUBLIC;
    }

    public void disable() {
        if (status != TemplateStatus.PUBLIC) {
            throw new IllegalStateException("공개 상태에서만 중단할 수 있음.");
        }
        this.status = TemplateStatus.DISABLED;
    }
}
