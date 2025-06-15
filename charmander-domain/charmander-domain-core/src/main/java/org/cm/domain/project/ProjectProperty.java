package org.cm.domain.project;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.With;
import org.cm.domain.common.ScreenSize;

@With
@Embeddable
public record ProjectProperty(
    @Embedded
    ScreenSize screenSize
) {
    public static ProjectProperty createDefault() {
        return new ProjectProperty(ScreenSize.createDefault());
    }
}
