package org.cm.api.project.dto;

import org.cm.domain.project.Project;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import static org.cm.api.project.dto.ProjectUpdateRequest.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record ProjectUpdateRequest(
    String name
) {
    @org.mapstruct.Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        void update(@MappingTarget Project e, ProjectUpdateRequest request);
    }

    public static void update(Project project, ProjectUpdateRequest request) {
        INSTANCE.update(project, request);
    }
}
