package org.cm.domain.scene;

import jakarta.persistence.Embeddable;
import lombok.With;
import org.cm.domain.common.ScreenSize;

@With
@Embeddable
public record SceneProperty(
    ScreenSize size,
    String background
) {
    public static SceneProperty createDefault() {
        return new SceneProperty(new ScreenSize(1920, 1080), "");
    }
}
