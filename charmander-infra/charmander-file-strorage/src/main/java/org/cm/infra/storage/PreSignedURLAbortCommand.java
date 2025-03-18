package org.cm.infra.storage;

public record PreSignedURLAbortCommand(
        String fileName,
        String uploadId
){
}
