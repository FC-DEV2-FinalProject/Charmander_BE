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
    public static ScreenSize createDefault() {
        return new ScreenSize(1920, 1080);
    }
}
