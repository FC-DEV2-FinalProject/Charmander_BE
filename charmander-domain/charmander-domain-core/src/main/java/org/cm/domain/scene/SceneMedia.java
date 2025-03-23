package org.cm.domain.scene;

import org.cm.domain.common.Position2D;
import org.cm.domain.common.ScreenSize;

import java.time.Duration;

public record SceneMedia(
    SceneMediaType type,
    String fileUrl,
    String startTimestamp,
    Duration duration,
    Property property
) {
    public record Property(
        int layer,
        ScreenSize size,
        Position2D position,
        double scale
    ) {
        public Property {
            scale = 1.0;
        }
    }
}
