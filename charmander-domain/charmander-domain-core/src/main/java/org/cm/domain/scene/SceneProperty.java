package org.cm.domain.scene;

import jakarta.persistence.Embeddable;
import org.cm.domain.common.ScreenSize;

@Embeddable
public record SceneProperty(
    ScreenSize size,
    String background
) {
}
