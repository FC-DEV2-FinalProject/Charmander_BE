package org.cm.domain.project;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import org.cm.domain.common.ScreenSize;

@Embeddable
public record ProjectProperty(
    @Embedded
    ScreenSize screenSize
) {
    public static ProjectProperty createDefault() {
        return new ProjectProperty(ScreenSize.createDefault());
    }
}
