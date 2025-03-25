package org.cm.domain.scene;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SceneTranscript extends BaseEntity {
    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Scene scene;

    @Column(nullable = false, length = 300)
    private String text;

    @Embedded
    private SceneTranscript.Property property;

    @Embeddable
    public record Property(
        double speed,
        int postDelay
    ) {
        public Property {
            speed = 1.0f;
        }
    }
}
