package org.cm.vo;

public record TtsCreateCommand(
        String text,
        String serverUrl,
        String fullPath,
        String fileName,
        String uploadId
) {

    public record TtsOption(float speed) {

    }
}
