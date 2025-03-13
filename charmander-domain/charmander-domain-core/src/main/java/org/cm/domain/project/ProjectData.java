package org.cm.domain.project;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cm.domain.common.GenericJsonConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectData {
    private List<ProjectScene> scenes = new ArrayList<>();

    public List<ProjectScene> getScenes() {
        return Collections.unmodifiableList(scenes);
    }

    public void addScene(ProjectScene scene) {
        scenes.add(scene);
    }

    public void removeScene(ProjectScene scene) {
        scenes.remove(scene);
    }

    public static ProjectData empty() {
        return new ProjectData();
    }

    public static class Converter extends GenericJsonConverter<ProjectData> {

    }
}
