package org.cm.api.project.dto;

import org.cm.domain.project.Project;
import org.cm.domain.project.ProjectData;
import org.jspecify.annotations.NonNull;

public record ProjectUpdateRequest(
    @NonNull
    String name,

    @NonNull
    ProjectData data
) {
    public void update(Project project) {
        project.setName(name());
        project.setData(data());
    }
}
