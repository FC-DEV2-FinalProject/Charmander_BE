package org.cm.domain.scene;

import java.time.Duration;
import org.cm.common.domain.SceneMediaType;
import org.cm.domain.common.Position2D;
import org.cm.domain.common.ScreenSize;

public record SceneMedia(
        SceneMediaType type,
        String fileId,
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
