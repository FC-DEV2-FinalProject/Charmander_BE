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
import org.cm.exception.CoreDomainException;
import org.cm.exception.CoreDomainExceptionCode;

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
            throw new CoreDomainException(CoreDomainExceptionCode.UPDATE_ALLOWED_ONLY_IN_DRAFT);
        }
        this.data = data;
    }

    public void publish() {
        if (status != TemplateStatus.DRAFT) {
            throw new CoreDomainException(CoreDomainExceptionCode.PUBLISH_ALLOWED_ONLY_IN_DRAFT);
        }
        this.status = TemplateStatus.PUBLIC;
    }

    public void disable() {
        if (status != TemplateStatus.PUBLIC) {
            throw new CoreDomainException(CoreDomainExceptionCode.DISABLE_ALLOWED_ONLY_IN_PUBLIC);
        }
        this.status = TemplateStatus.DISABLED;
    }
}
