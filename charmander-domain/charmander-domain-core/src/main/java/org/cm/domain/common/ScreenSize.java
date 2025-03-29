package org.cm.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ScreenSize(
    @Column(nullable = false)
    int width,

    @Column(nullable = false)
    int height
) {
}
