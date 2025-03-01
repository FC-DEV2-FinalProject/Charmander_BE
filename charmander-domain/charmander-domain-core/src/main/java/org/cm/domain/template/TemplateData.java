package org.cm.domain.template;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.cm.domain.common.ScreenSize;

@Embeddable
public record TemplateData(
        @Column(nullable = false)
        TemplateCategory category,

        @Column(nullable = false)
        String name,

        @Embedded
        ScreenSize size,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, updatable = false)
        TemplateBackground background,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, updatable = false)
        TemplateAvatar avatar
) {
}
