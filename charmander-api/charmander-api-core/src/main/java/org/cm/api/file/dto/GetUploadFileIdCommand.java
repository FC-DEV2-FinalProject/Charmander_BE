package org.cm.api.file.dto;

import jakarta.validation.constraints.NotEmpty;
import org.cm.common.domain.FileType;

public record GetUploadFileIdCommand(
    @NotEmpty
    String fileName,

    FileType fileType
) {
}
