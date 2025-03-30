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
    public static SceneMedia createDefault() {
        return new SceneMedia(
                SceneMediaType.Image,
                "",
                "0",
                Duration.ZERO,
                Property.createDefault()
        );
    }

    public record Property(
            int layer,
            ScreenSize size,
            Position2D position,
            double scale
    ) {
        public Property {
            scale = 1.0;
        }

        public static Property createDefault() {
            return new Property(0, new ScreenSize(1280, 720), new Position2D(0, 0), 1.0);
        }

        public static Property of(ScreenSize size) {
            return new Property(0, size, new Position2D(0, 0), 1.0);
        }
    }
}
