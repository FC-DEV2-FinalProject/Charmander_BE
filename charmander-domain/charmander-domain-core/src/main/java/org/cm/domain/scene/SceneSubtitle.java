package org.cm.domain.scene;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SceneSubtitle {
    @Column(name = "subtitle_text", nullable = false, length = 300)
    private String text;

    @Embedded
    private SceneSubtitle.Property property;

    @Embeddable
    public record Property(
        String fontFamilly,
        String fontSize,
        String fontColor
    ) {
        public static SceneSubtitle.Property createDefault() {
            return new SceneSubtitle.Property("Arial", "12", "#FFFFFF");
        }
    }

    public SceneSubtitle(String text, SceneSubtitle.Property property) {
        this.text = text;
        this.property = property;
    }

    public static SceneSubtitle createDefault() {
        return new SceneSubtitle("", SceneSubtitle.Property.createDefault());
    }
}
