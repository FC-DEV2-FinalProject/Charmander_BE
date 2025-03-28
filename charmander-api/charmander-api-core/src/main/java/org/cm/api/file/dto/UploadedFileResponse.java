package org.cm.api.file.dto;

import org.cm.domain.file.UploadedFile;
import org.mapstruct.Mapper;

import static org.cm.api.file.dto.UploadedFileResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record UploadedFileResponse(
    String fileId
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        UploadedFileResponse map(UploadedFile e);
    }

    public static UploadedFileResponse from(UploadedFile e) {
        return INSTANCE.map(e);
    }
}
