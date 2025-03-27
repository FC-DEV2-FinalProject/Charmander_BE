package org.cm.domain.scene;

import jakarta.persistence.*;
import lombok.*;
import org.cm.domain.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SceneTranscript extends BaseEntity {
    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Scene scene;

    @Setter
    @Column(nullable = false, length = 300)
    private String text;

    @Setter
    @Embedded
    private SceneTranscript.Property property;

    public SceneTranscript(Scene scene, String text, Property property) {
        this.scene = scene;
        this.text = text;
        this.property = property;
    }

    @With
    @Embeddable
    public record Property(
        @Column(nullable = false)
        Double speed,
        @Column(nullable = false)
        Integer postDelay
    ) {
        public Property {
            if (speed == null) {
                speed = 1.0;
            }

            if (postDelay == null) {
                postDelay = 0;
            }
        }

        public static Property createDefault() {
            return new Property(1.0, 0);
        }
    }
}
