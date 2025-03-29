package org.cm.infra.storage;

import java.util.List;
import software.amazon.awssdk.services.s3.model.CompletedPart;

public record PreSignedURLCompleteCommand(
        String fileName,
        String uploadId,
        List<Part> parts
){

    public List<CompletedPart> toParts() {
        return parts.stream()
                .map(part -> CompletedPart.builder()
                        .eTag(part.eTag())
                        .partNumber(part.partNumber())
                        .build())
                .toList();
    }

    public record Part(int partNumber, String eTag) {
    }
}
