package org.cm.api.project.dto;

import org.cm.domain.common.ScreenSize;
import org.cm.domain.project.Project;

import java.io.Serializable;

public record ProjectUpdateRequest(
    String name,
    ProjectPropertyDto property
) {
    public static void update(Project project, ProjectUpdateRequest request) {
        updateName(project, request.name());
        updateProperty(project, request.property());
    }

    private static void updateName(Project project, String name) {
        if (name == null) {
            return;
        }

        project.setName(name);
    }

    private static void updateProperty(Project project, ProjectPropertyDto property) {
        if (property == null) {
            return;
        }

        var p = project.getProperty();

        if (property.screenSize() != null) {
            p = p.withScreenSize(property.screenSize().toEntity(p.screenSize()));
        }

        project.setProperty(p);
    }


    public record ProjectPropertyDto(
        ScreenSizeDto screenSize
    ) implements Serializable {
        public record ScreenSizeDto(
            Integer width,
            Integer height
        ) implements Serializable {
            public ScreenSize toEntity(ScreenSize prev) {
                return new ScreenSize(
                    width == null ? prev.width() : width,
                    height == null ? prev.height() : height
                );
            }
        }
    }
}
