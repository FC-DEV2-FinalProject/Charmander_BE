package org.cm.test.fixture;

import org.cm.domain.project.Project;
import org.cm.domain.scene.Scene;
import org.cm.domain.scene.SceneMedia;
import org.cm.domain.scene.SceneProperty;
import org.cm.domain.scene.SceneSubtitle;

public class SceneFixture {
    public static Scene create() {
        return create(ProjectFixture.create());
    }

    public static Scene create(Project project) {
        var background = SceneMedia.createDefault();
        var avatar = SceneMedia.createDefault();
        return create(project, background, avatar);
    }

    public static Scene create(Project project, SceneMedia background) {
        var subtitle = SceneSubtitle.createDefault();
        var avatar = SceneMedia.createDefault();
        var property = SceneProperty.createDefault();
        return new Scene(project, subtitle, background, avatar, property);
    }

    public static Scene create(Project project, SceneMedia background, SceneMedia avatar) {
        var subtitle = SceneSubtitle.createDefault();
        var property = SceneProperty.createDefault();
        return new Scene(project, subtitle, background, avatar, property);
    }

    public static Scene create(Project project, SceneMedia background, SceneMedia avatar, SceneProperty property) {
        var subtitle = SceneSubtitle.createDefault();
        return new Scene(project, subtitle, background, avatar, property);
    }

    public static Scene create(Project project, SceneSubtitle subtitle, SceneMedia background, SceneMedia avatar, SceneProperty property) {
        return new Scene(project, subtitle, background, avatar, property);
    }
}
