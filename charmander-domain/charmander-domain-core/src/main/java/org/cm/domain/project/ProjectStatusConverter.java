package org.cm.domain.project;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProjectStatusConverter implements AttributeConverter<ProjectStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProjectStatus status) {
        return status.toString();
    }

    @Override
    public ProjectStatus convertToEntityAttribute(String dbData) {
        return ProjectStatus.valueOf(dbData);
    }
}
