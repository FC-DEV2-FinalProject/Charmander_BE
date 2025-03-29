package org.cm.api.file.dto;

import org.cm.domain.file.UploadedFile;

import static org.cm.api.file.dto.GetUploadFileIdResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record GetUploadFileIdResponse(
    String fullPath,
    String fileName,
    String uploadId
) {
    @org.mapstruct.Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR
    )
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        GetUploadFileIdResponse map(UploadedFile e);
    }

    public static GetUploadFileIdResponse from(UploadedFile e) {
        return INSTANCE.map(e);
    }
}
