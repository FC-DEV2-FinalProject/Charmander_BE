package org.cm.infra.storage;

public record PreSignedURLIdentifier(
        String fullPath,
        String fileName,
        String uploadId
){
}
