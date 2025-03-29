package org.cm.api.file.dto;

import org.cm.domain.file.UploadedFile;
import org.cm.domain.file.UploadedFileType;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

import static org.cm.api.file.dto.UploadedFileResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record UploadedFileResponse(
    String fullPath,
    String uploadId,
    UploadedFileType uploadType,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
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
