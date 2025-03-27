package org.cm.test.fixture;

import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneTranscript;

public class SceneTranscriptFixture {
    public static SceneTranscript create() {
        return create(SceneFixture.create(), "안녕하세요. 반가워요.", new SceneTranscript.Property(1.0, 0));
    }

    public static SceneTranscript create(Scene scene, String text, SceneTranscript.Property property) {
        return new SceneTranscript(scene, text, property);
    }
}
