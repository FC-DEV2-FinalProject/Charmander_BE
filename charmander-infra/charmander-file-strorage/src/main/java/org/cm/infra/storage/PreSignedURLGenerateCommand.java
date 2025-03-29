package org.cm.infra.storage;

public record PreSignedURLGenerateCommand(
        String fileName,
        String uploadId,
        int partNumber
){
}
